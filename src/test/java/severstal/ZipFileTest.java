package severstal;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFileTest {

    @Test
    @DisplayName("Тестирование зазипованных файлов по содержанию")
    void zipFileTest() throws Exception {

        ZipFile zipFile = new ZipFile("src/test/resources/zip_sample.zip");
        ZipEntry zipEntry;

        zipEntry = zipFile.getEntry("sample.csv");
        try (InputStream stream = zipFile.getInputStream(zipEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            List<String[]> list = reader.readAll();
            assertThat(list).hasSize(3).contains(
                    new String[] {"cpu", "gpu"},
                    new String[] {"amd","nvidia"},
                    new String[] {"let's go", "yup"}
            );
        }

        zipEntry = zipFile.getEntry("sample.xlsx");
        try (InputStream stream = zipFile.getInputStream(zipEntry)) {
            XLS parsedXLS = new XLS(stream);
            assertThat(parsedXLS.excel
                    .getSheetAt(1)
                    .getRow(0)
                    .getCell(0)
                    .getStringCellValue())
                    .isEqualTo("Bugaga");
        }

        zipEntry = zipFile.getEntry("Java.pdf");
        try (InputStream stream = zipFile.getInputStream(zipEntry)) {
            PDF parsedPDF = new PDF(stream);
            assertThat(parsedPDF.title).contains("Addressing");
        }
    }
}
