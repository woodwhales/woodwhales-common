package cn.woodwhales.common.util.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * excel 画斜线上下文对象
 *
 * @author woodwhales
 */
public class DrawSlashContext {
    /**
     * 文件输出流
     */
    public OutputStream outputStream;

    /**
     * excel 文件对象
     */
    public Workbook workbook;

    /**
     * sheet 对象
     */
    public Sheet sheet;

    /**
     * 起始列索引
     */
    public int col1;

    /**
     * 起始行索引
     */
    public int row1;

    /**
     * 结束列索引
     */
    public int col2;

    /**
     * 结束行索引
     */
    public int row2;

    /**
     * 导出指指定文件流
     * @throws IOException IOException
     */
    public void export() throws IOException {
        workbook.write(this.outputStream);
        this.outputStream.flush();
        workbook.close();
        this.outputStream.close();
    }
}