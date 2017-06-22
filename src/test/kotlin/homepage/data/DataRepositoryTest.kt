package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.io.File
import java.nio.file.Files

internal class DataRepositoryTest {

    val log: Logger = getLogger(javaClass)

    val mapper = ObjectMapper()
    val settings = DataRepository.Settings()

    val workingDirectory = Files.createTempDirectory("test-git-repo").toFile()!!.apply { deleteOnExit() }
    val repository: Repository = mock { on { workTree } doReturn workingDirectory }
    val git: Git = mock { on { repository } doReturn repository }

    val cut = DataRepository(mapper, settings, git)

    @Nested
    inner class `find datum` {

        init {
            createTestFile(fileName = "the-testfile.json")
            createTestFile(fileName = "another-testfile.json")
            createTestFile(path = "subdir", fileName = "the-testfile.json")
        }

        @Test
        fun `single datum can be found on root of repository`() {
            val found = cut.findDatum(fileName = "the-testfile.json", dataClass = TestFile::class)
            assertThat(found).isNotNull()
            assertThat(found!!.foo).isEqualTo("the-testfile.json")
        }

        @Test
        fun `single datum can be found in sub-directory of repository`() {
            val found = cut.findDatum(path = "subdir", fileName = "the-testfile.json", dataClass = TestFile::class)
            assertThat(found).isNotNull()
            assertThat(found!!.foo).isEqualTo("subdir/the-testfile.json")
        }

        @Test
        fun `folder with same name is ignored`() {
            val found = cut.findDatum(fileName = "subdir", dataClass = TestFile::class)
            assertThat(found).isNull()
        }

    }

    @Nested
    inner class `find data` {

        init {
            createTestFile(fileName = "the-testfile.json")
            createTestFile(fileName = "another-testfile.json")
            createTestFile(path = "subdir", fileName = "the-testfile.json")
        }

        @Test
        fun `single datum can be found on root of repository`() {
            val found = cut.findData(extension = "json", dataClass = TestFile::class)
            assertThat(found).hasSize(2)
        }

        @Test
        fun `single datum can be found in sub-directory of repository`() {
            val found = cut.findData(path = "subdir", extension = "json", dataClass = TestFile::class)
            assertThat(found).hasSize(1)
        }

    }

    fun createTestFile(path: String = "", fileName: String) {
        val directory = if (path.isNotBlank()) workingDirectory.resolve(path) else workingDirectory
        if (!directory.exists() && directory.mkdirs()) {
            log.debug("created directory: {}", directory)
        }
        val anotherTestFile = File(directory, fileName)
        val prefix = if (path.isNotBlank()) "$path/" else ""
        mapper.writeValue(anotherTestFile, TestFile("$prefix$fileName"))
    }

    data class TestFile(var foo: String = "bar")

}