package net.equipment.services;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCodeGenerator {

    public static void generateQRCode(String text, String fileName) throws WriterException, IOException {
        // Путь к папке static/qr-codes
        Path directory = Paths.get("src/main/resources/static");

        // Создаем директорию, если она не существует
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Формируем полный путь к файлу
        Path filePath = directory.resolve(fileName);

        // Генерируем QR-код
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        // Записываем QR-код в файл
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            Files.write(filePath, out.toByteArray());
        }
    }
}
