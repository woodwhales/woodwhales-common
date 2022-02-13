package cn.woodwhales.common.file;

import cn.hutool.core.util.ZipUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * <p>
 * 文件加解密工具
 * </p>
 * <p>
 * 原始文件：123.zip
 * 加密之后：123_89504E47_504b0304_zip.PNG (该文件无法正常打开)
 * 解密文件：123.zip
 * </p>
 *
 * @author woodwhales on 2021-10-01 1:26
 */
public class FileEncryptTool {

    /**
     * 加密文件，将文件加密成 png 格式的文件
     *
     * @param originFileName 要加密的文件绝对路径
     * @return 已成功加密的新文件名
     */
    public static String encodeFileToPng(String originFileName) {
        return encodeFile(originFileName, FileTypeHexStrEnum.PNG);
    }

    /**
     * <p>
     * 加密文件，将按照 fileTypeHexStrEnum.hexStr 加密 originFileName 文件
     * </p>
     * <p>
     * 加密成功之后，文件变为指定 fileTypeHexStrEnum.suffix 的后缀名
     * </p>
     *
     * @param originFileName     要加密的文件绝对路径
     * @param fileTypeHexStrEnum 要加密的条件枚举
     * @return 已成功加密的新文件名
     */
    public static String encodeFile(String originFileName, FileTypeHexStrEnum fileTypeHexStrEnum) {
        File originFile = new File(originFileName);
        if (!originFile.exists()) {
            throw new RuntimeException(originFile + " 文件不存在");
        }

        String suffix = StringUtils.substringAfterLast(originFileName, ".");
        String fileName = StringUtils.substringBeforeLast(originFileName, ".");


        if (!StringUtils.equalsIgnoreCase(suffix, "zip")) {
            File zipFile = ZipUtil.zip(originFileName, fileName + ".zip");
            originFileName = zipFile.getAbsolutePath();
        }
        return encode(originFileName, fileTypeHexStrEnum.hexStr, fileTypeHexStrEnum.suffix);
    }

    /**
     * 解密文件，将读取 encodeOriginFileName 的文件名信息，进行解密操作
     *
     * @param encodeOriginFileName 要解密的文件（文件已加密）绝对路径
     * @return 解密成功之后的文件名
     */
    public static String decodeFile(String encodeOriginFileName) {
        String decodeZipFile = decode(encodeOriginFileName);
        File unzipFile = ZipUtil.unzip(decodeZipFile);
        return unzipFile.getAbsolutePath();
    }

    private FileEncryptTool() {
    }

    /**
     * <p>
     * 加密文件，将按照 encodeHexStr 加密 originFileName 文件，
     * </p>
     * <p>
     * 加密成功之后，文件变为指定 encodeSuffix 的后缀名
     * </p>
     *
     * @param originFileName 要加密的文件绝对路径
     * @param encodeHexStr   要加密的 HEX 字节信息
     * @param encodeSuffix   要加密成的文件后缀
     * @return 已成功加密的新文件名
     */
    private static String encode(String originFileName, String encodeHexStr, String encodeSuffix) {
        File originFile = new File(originFileName);
        if (!originFile.exists()) {
            throw new RuntimeException(originFile + " 文件不存在");
        }

        String subOriginFileName = StringUtils.substringBeforeLast(originFileName, ".");
        String subOriginFileSuffix = StringUtils.substringAfterLast(originFileName, ".");

        String originHexStr = null;

        try (
                RandomAccessFile randomAccessFile = new RandomAccessFile(originFile, "rw")) {
            // 读取原始字节信息
            byte[] buffer = new byte[4];
            randomAccessFile.seek(0);
            randomAccessFile.read(buffer, 0, buffer.length);
            originHexStr = Hex.encodeHexString(buffer);

            // 填充加密字节信息
            byte[] fillBuffer = Hex.decodeHex(encodeHexStr);
            randomAccessFile.seek(0);
            randomAccessFile.write(fillBuffer, 0, fillBuffer.length);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String encodedFileName = "" + subOriginFileName
                + "_"
                + encodeHexStr
                + "_"
                + originHexStr
                + "_"
                + subOriginFileSuffix
                + "."
                + encodeSuffix;
        originFile.renameTo(new File(encodedFileName));
        return encodedFileName;
    }

    /**
     * 解密文件，将读取 encodeOriginFileName 的文件名信息，进行解密操作
     *
     * @param encodeOriginFileName 要解密的文件（文件已加密）绝对路径
     * @return 解密成功之后的文件名
     */
    private static String decode(String encodeOriginFileName) {
        File encodeOriginFile = new File(encodeOriginFileName);
        if (!encodeOriginFile.exists()) {
            new RuntimeException(encodeOriginFileName + " 文件不存在");
        }

        String encodeOriginFileNameWithoutSuffix = StringUtils.substringBeforeLast(encodeOriginFileName, ".");
        String encodeOriginFileNameSuffix = StringUtils.substringAfterLast(encodeOriginFileName, ".");
        String[] split = StringUtils.split(encodeOriginFileNameWithoutSuffix, "_");
        String originFileSuffix = split[split.length - 1];
        String decodeHexStr = split[split.length - 2];
        String encodedHexStr = split[split.length - 3];

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(encodeOriginFile, "rw")) {
            byte[] fillBytes = Hex.decodeHex(decodeHexStr);
            randomAccessFile.seek(0);
            randomAccessFile.write(fillBytes, 0, fillBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String tempStr = "_" + encodedHexStr + "_" + decodeHexStr + "_" + originFileSuffix + "." + encodeOriginFileNameSuffix;
        String newFileName = StringUtils.replace(encodeOriginFileName, tempStr, "." + originFileSuffix);
        encodeOriginFile.renameTo(new File(newFileName));
        return newFileName;
    }

}
