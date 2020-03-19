package application.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;

import com.sun.javafx.scene.control.skin.TableViewSkin;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;

public class GUIUtils {

	private static Method columnToFitMethod;
	
	  static {
	        try {
	            columnToFitMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
	            columnToFitMethod.setAccessible(true);
	        } catch (NoSuchMethodException e) {
	            e.printStackTrace();
	        }
	    }

	/*
	 * public static void autoFitTable(TableView tableView) {
	 * tableView.getItems().addListener(new ListChangeListener<Object>() {
	 * 
	 * @Override public void onChanged(Change<?> c) { for (Object column :
	 * tableView.getColumns()) { try { columnToFitMethod.invoke(tableView.getSkin(),
	 * column, -1); } catch (IllegalAccessException | InvocationTargetException e) {
	 * e.printStackTrace(); } } } }); }
	 */
	  
	  public static byte[] readBytesFromFile(File file) throws IOException {
	        FileInputStream inputStream = new FileInputStream(file);
	         
	        byte[] fileBytes = new byte[(int) file.length()];
	        inputStream.read(fileBytes);
	        inputStream.close();
	         
	        return fileBytes;
	    }
	  
	  public static void saveBytesToFile(String filePath, byte[] fileBytes) throws IOException {
	        FileOutputStream outputStream = new FileOutputStream(filePath);
	        outputStream.write(fileBytes);
	        outputStream.close();
	    }
	  
	  public static byte[] readBytesFromImage(Image image) throws IOException {
		  BufferedImage bImg = SwingFXUtils.fromFXImage(image, null);
		  ByteArrayOutputStream out = new ByteArrayOutputStream();
		  ImageIO.write(bImg, "jpg", out);
		  byte[] res = out.toByteArray();
		  out.close();
		  return res;
	  }
	  
	  public static Image readImageFromByte(byte[] imgByte) throws IOException {
		  ByteArrayInputStream bis = new ByteArrayInputStream(imgByte);
		  BufferedImage bImage = ImageIO.read(bis);
		  Image img = SwingFXUtils.toFXImage(bImage, null);
		  return img;
	  }
}
