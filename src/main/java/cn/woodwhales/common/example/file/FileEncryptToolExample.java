package cn.woodwhales.common.example.file;

import cn.woodwhales.common.file.FileEncryptTool;
import cn.woodwhales.common.file.FileTypeHexStrEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;

import static cn.woodwhales.common.file.FileEncryptTool.encodeFile;
import static cn.woodwhales.common.file.FileEncryptTool.encodeFileToPng;

/**
 * FileEncryptTool 使用示例
 *
 * @author woodwhales created on 2021-10-01 17:53
 */
class FileEncryptToolExample {

    private static String originFileName;
    private static String originFileParentAbsolutePath;

    public static void main(String[] args) {
        // 加密
        testEncoder();
        // 解密
        testDecode();
    }

    private static void init() {
        URL resource = FileEncryptToolExample.class
                .getClassLoader()
                .getResource("test.png");

        originFileName = resource.getFile();
        File parentFile = new File(originFileName).getParentFile();
        originFileParentAbsolutePath = parentFile.getAbsolutePath();
    }

    /**
     * 加密测试
     */
    public static void testEncoder() {
        init();
        String encodeFileName = encodeFileToPng(FileEncryptToolExample.originFileName);
        assertEquals("test_89504E47_504b0304_zip.PNG", StringUtils.substringAfterLast(encodeFileName, File.separator));
    }

    /**
     * 解密测试
     */
    public static void testDecode() {
        init();
        String encodeFileName = encodeFile(FileEncryptToolExample.originFileName,
                FileTypeHexStrEnum.PNG);
        String decodeFileName = FileEncryptTool.decodeFile(encodeFileName);
        assertEquals(originFileParentAbsolutePath + File.separator + "test", decodeFileName);
    }

    private static void assertEquals(Object expected, Object actual) {
        boolean b = objectsAreEqual(expected, actual);
        if (!b) {
            throw new RuntimeException("\nexpected = " + expected + ", actual = " + actual);
        }
    }

    private static boolean objectsAreEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return (obj2 == null);
        }
        return obj1.equals(obj2);
    }

}