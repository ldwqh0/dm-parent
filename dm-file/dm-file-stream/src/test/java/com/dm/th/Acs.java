//package com.dm.th;
//
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.Java2DFrameConverter;
//import org.junit.jupiter.api.Test;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//
//public class Acs {
//
//    @Test
//    public void tst() throws IOException, InterruptedException {
//        Process process = Runtime.getRuntime().exec(new String[]{
//            "ffmpeg",
//            "-i",
//            "d:/in.jpg",
//            "-vf",
//            "scale=-1:150",
//            "-y",
//            "d:/out.png",
//        });
//
////        Process process = Runtime.getRuntime().exec("ffmpeg -i d:/in.jpg -vf scale=-1:150 -y d:/out.png");
//        BufferedInputStream bis = new BufferedInputStream(process.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(bis));
//        String line;
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//        int v = process.waitFor();
//        System.out.println(v);
//    }
//
//    @Test
//    public void test2() throws IOException {
////        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File("D:/in.jpg"));
////        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File("D:/BaiduNetdiskDownload/阆水歌.mp4"));
//        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File("D:/in.bmp"));
//        grabber.start();
//        String format = grabber.getFormat();
//        int height = grabber.getImageHeight();
//        int width = grabber.getImageWidth();
//        int number = grabber.getFrameNumber();
//        Frame frame = grabber.grabFrame();
//        Java2DFrameConverter converter = new Java2DFrameConverter();
//        BufferedImage image = converter.convert(frame);
//        ImageIO.write(image, "jpg", new File("d:/out.jpg"));
//
//        System.out.println(height);
//        grabber.stop();
//        grabber.release();
//        ;
//    }
//
//    @Test
//    public void test3() throws FFmpegFrameGrabber.Exception {
//        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File("D:/BaiduNetdiskDownload/阆水歌.mp4"));
////        FFmpegFrameGrabber.createDefault()
//
//        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(new File("D:/out.mp4"), grabber.getAudioChannels());
//        grabber.start();
//grabber.grabImage();
//
//    }
//}
