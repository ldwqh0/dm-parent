package com.dm.file.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "file")
public class FileConfig {
  private String path;
  private Map<String, String> mime;
  private String tempPath;

  public FileConfig() {
    this.path = System.getProperty("user.home") + File.separator + "uploads";
    tempPath = System.getProperty("java.io.tmpdir") + File.separator + System.currentTimeMillis() + File.separator;
    this.mime = new HashMap<>();
    this.mime.put("323", "text/h323");
    this.mime.put("3gp", "video/3gpp");
    this.mime.put("acx", "application/internet-property-stream");
    this.mime.put("ai", "application/postscript");
    this.mime.put("aif", "audio/x-aiff");
    this.mime.put("aifc", "audio/x-aiff");
    this.mime.put("aiff", "audio/x-aiff");
    this.mime.put("asf", "video/x-ms-asf");
    this.mime.put("asr", "video/x-ms-asf");
    this.mime.put("asx", "video/x-ms-asf");
    this.mime.put("au", "audio/basic");
    this.mime.put("avi", "video/x-msvideo");
    this.mime.put("axs", "application/olescript");
    this.mime.put("bas", "text/plain");
    this.mime.put("bcpio", "application/x-bcpio");
    this.mime.put("bin", "application/octet-stream");
    this.mime.put("bmp", "image/bmp");
    this.mime.put("c", "text/plain");
    this.mime.put("cat", "application/vnd.ms-pkiseccat");
    this.mime.put("cdf", "application/x-cdf");
    this.mime.put("cer", "application/x-x509-ca-cert");
    this.mime.put("class", "application/octet-stream");
    this.mime.put("clp", "application/x-msclip");
    this.mime.put("cmx", "image/x-cmx");
    this.mime.put("cod", "image/cis-cod");
    this.mime.put("cpio", "application/x-cpio");
    this.mime.put("crd", "application/x-mscardfile");
    this.mime.put("crl", "application/pkix-crl");
    this.mime.put("crt", "application/x-x509-ca-cert");
    this.mime.put("csh", "application/x-csh");
    this.mime.put("css", "text/css");
    this.mime.put("dcr", "application/x-director");
    this.mime.put("der", "application/x-x509-ca-cert");
    this.mime.put("dir", "application/x-director");
    this.mime.put("dll", "application/x-msdownload");
    this.mime.put("dms", "application/octet-stream");
    this.mime.put("doc", "application/msword");
    this.mime.put("dot", "application/msword");
    this.mime.put("dvi", "application/x-dvi");
    this.mime.put("dxr", "application/x-director");
    this.mime.put("eps", "application/postscript");
    this.mime.put("etx", "text/x-setext");
    this.mime.put("evy", "application/envoy");
    this.mime.put("exe", "application/octet-stream");
    this.mime.put("fif", "application/fractals");
    this.mime.put("flr", "x-world/x-vrml");
    this.mime.put("flv", "video/x-flv");
    this.mime.put("gif", "image/gif");
    this.mime.put("gtar", "application/x-gtar");
    this.mime.put("gz", "application/x-gzip");
    this.mime.put("h", "text/plain");
    this.mime.put("hdf", "application/x-hdf");
    this.mime.put("hlp", "application/winhlp");
    this.mime.put("hqx", "application/mac-binhex40");
    this.mime.put("hta", "application/hta");
    this.mime.put("htc", "text/x-component");
    this.mime.put("htm", "text/html");
    this.mime.put("html", "text/html");
    this.mime.put("htt", "text/webviewhtml");
    this.mime.put("ico", "image/x-icon");
    this.mime.put("ief", "image/ief");
    this.mime.put("iii", "application/x-iphone");
    this.mime.put("ins", "application/x-internet-signup");
    this.mime.put("isp", "application/x-internet-signup");
    this.mime.put("jfif", "image/pipeg");
    this.mime.put("jpe", "image/jpeg");
    this.mime.put("jpeg", "image/jpeg");
    this.mime.put("jpg", "image/jpeg");
    this.mime.put("js", "application/x-javascript");
    this.mime.put("latex", "application/x-latex");
    this.mime.put("lha", "application/octet-stream");
    this.mime.put("lsf", "video/x-la-asf");
    this.mime.put("lsx", "video/x-la-asf");
    this.mime.put("lzh", "application/octet-stream");
    this.mime.put("m13", "application/x-msmediaview");
    this.mime.put("m14", "application/x-msmediaview");
    this.mime.put("m3u", "audio/x-mpegurl");
    this.mime.put("m3u8", "application/x-mpegURL");
    this.mime.put("man", "application/x-troff-man");
    this.mime.put("mdb", "application/x-msaccess");
    this.mime.put("me", "application/x-troff-me");
    this.mime.put("mht", "message/rfc822");
    this.mime.put("mhtml", "message/rfc822");
    this.mime.put("mid", "audio/mid");
    this.mime.put("mny", "application/x-msmoney");
    this.mime.put("mov", "video/quicktime");
    this.mime.put("movie", "video/x-sgi-movie");
    this.mime.put("mp2", "video/mpeg");
    this.mime.put("mp3", "audio/mpeg");
    this.mime.put("mp4", "  video/mp4");
    this.mime.put("mpa", "video/mpeg");
    this.mime.put("mpe", "video/mpeg");
    this.mime.put("mpeg", "video/mpeg");
    this.mime.put("mpg", "video/mpeg");
    this.mime.put("mpp", "application/vnd.ms-project");
    this.mime.put("mpv2", "video/mpeg");
    this.mime.put("ms", "application/x-troff-ms");
    this.mime.put("mvb", "application/x-msmediaview");
    this.mime.put("nws", "message/rfc822");
    this.mime.put("oda", "application/oda");
    this.mime.put("p10", "application/pkcs10");
    this.mime.put("p12", "application/x-pkcs12");
    this.mime.put("p7b", "application/x-pkcs7-certificates");
    this.mime.put("p7c", "application/x-pkcs7-mime");
    this.mime.put("p7m", "application/x-pkcs7-mime");
    this.mime.put("p7r", "application/x-pkcs7-certreqresp");
    this.mime.put("p7s", "application/x-pkcs7-signature");
    this.mime.put("pbm", "image/x-portable-bitmap");
    this.mime.put("pdf", "application/pdf");
    this.mime.put("pfx", "application/x-pkcs12");
    this.mime.put("pgm", "image/x-portable-graymap");
    this.mime.put("pko", "application/ynd.ms-pkipko");
    this.mime.put("pma", "application/x-perfmon");
    this.mime.put("pmc", "application/x-perfmon");
    this.mime.put("pml", "application/x-perfmon");
    this.mime.put("pmr", "application/x-perfmon");
    this.mime.put("pmw", "application/x-perfmon");
    this.mime.put("png", "image/png");
    this.mime.put("pnm", "image/x-portable-anymap");
    this.mime.put("pot,", "application/vnd.ms-powerpoint");
    this.mime.put("ppm", "image/x-portable-pixmap");
    this.mime.put("pps", "application/vnd.ms-powerpoint");
    this.mime.put("ppt", "application/vnd.ms-powerpoint");
    this.mime.put("prf", "application/pics-rules");
    this.mime.put("ps", "application/postscript");
    this.mime.put("pub", "application/x-mspublisher");
    this.mime.put("qt", "video/quicktime");
    this.mime.put("ra", "audio/x-pn-realaudio");
    this.mime.put("ram", "audio/x-pn-realaudio");
    this.mime.put("ras", "image/x-cmu-raster");
    this.mime.put("rgb", "image/x-rgb");
    this.mime.put("rmi", "audio/mid");
    this.mime.put("roff", "application/x-troff");
    this.mime.put("rtf", "application/rtf");
    this.mime.put("rtx", "text/richtext");
    this.mime.put("scd", "application/x-msschedule");
    this.mime.put("sct", "text/scriptlet");
    this.mime.put("setpay", "application/set-payment-initiation");
    this.mime.put("setreg", "application/set-registration-initiation");
    this.mime.put("sh", "application/x-sh");
    this.mime.put("shar", "application/x-shar");
    this.mime.put("sit", "application/x-stuffit");
    this.mime.put("snd", "audio/basic");
    this.mime.put("spc", "application/x-pkcs7-certificates");
    this.mime.put("spl", "application/futuresplash");
    this.mime.put("src", "application/x-wais-source");
    this.mime.put("sst", "application/vnd.ms-pkicertstore");
    this.mime.put("stl", "application/vnd.ms-pkistl");
    this.mime.put("stm", "text/html");
    this.mime.put("svg", "image/svg+xml");
    this.mime.put("sv4cpio", "application/x-sv4cpio");
    this.mime.put("sv4crc", "application/x-sv4crc");
    this.mime.put("swf", "application/x-shockwave-flash");
    this.mime.put("t", "application/x-troff");
    this.mime.put("tar", "application/x-tar");
    this.mime.put("tcl", "application/x-tcl");
    this.mime.put("tex", "application/x-tex");
    this.mime.put("texi", "application/x-texinfo");
    this.mime.put("texinfo", "application/x-texinfo");
    this.mime.put("tgz", "application/x-compressed");
    this.mime.put("tif", "image/tiff");
    this.mime.put("tiff", "image/tiff");
    this.mime.put("tr", "application/x-troff");
    this.mime.put("trm", "application/x-msterminal");
    this.mime.put("ts", "video/MP2T");
    this.mime.put("tsv", "text/tab-separated-values");
    this.mime.put("txt", "text/plain");
    this.mime.put("uls", "text/iuls");
    this.mime.put("ustar", "application/x-ustar");
    this.mime.put("vcf", "text/x-vcard");
    this.mime.put("vrml", "x-world/x-vrml");
    this.mime.put("wav", "audio/x-wav");
    this.mime.put("wcm", "application/vnd.ms-works");
    this.mime.put("wdb", "application/vnd.ms-works");
    this.mime.put("wks", "application/vnd.ms-works");
    this.mime.put("wmf", "application/x-msmetafile");
    this.mime.put("wmv", "video/x-ms-wmv");
    this.mime.put("wps", "application/vnd.ms-works");
    this.mime.put("wri", "application/x-mswrite");
    this.mime.put("wrl", "x-world/x-vrml");
    this.mime.put("wrz", "x-world/x-vrml");
    this.mime.put("xaf", "x-world/x-vrml");
    this.mime.put("xbm", "image/x-xbitmap");
    this.mime.put("xla", "application/vnd.ms-excel");
    this.mime.put("xlc", "application/vnd.ms-excel");
    this.mime.put("xlm", "application/vnd.ms-excel");
    this.mime.put("xls", "application/vnd.ms-excel");
    this.mime.put("xlt", "application/vnd.ms-excel");
    this.mime.put("xlw", "application/vnd.ms-excel");
    this.mime.put("xof", "x-world/x-vrml");
    this.mime.put("xpm", "image/x-xpixmap");
    this.mime.put("xwd", "image/x-xwindowdump");
    this.mime.put("z", "application/x-compress");
    this.mime.put("zip", "application/zip");
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getMime(String ext) {
    String result = this.mime.get(ext);
    return StringUtils.isEmpty(result) ? "application/octet-stream" : result;
  }

  public String getTempPath() {
    return tempPath;
  }

  public void setTempPath(String tempPath) {
    this.tempPath = tempPath;
  }

  public void setMime(Map<String, String> mime) {
    if (MapUtils.isNotEmpty(mime)) {
      for (Map.Entry<String, String> entity : mime.entrySet()) {
        this.mime.put(entity.getKey(), entity.getValue());
      }
    }
  }
}
