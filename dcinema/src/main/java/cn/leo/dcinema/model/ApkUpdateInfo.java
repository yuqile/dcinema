package cn.leo.dcinema.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ApkUpdateInfo
  implements Serializable
{
  private static final long serialVersionUID = -6652507875072567089L;
  public String apkmd5;
  public String apkpath;
  public String apkurl;
  public String appname;
  public String instruction;
  public int verCode;
  public String verName;
}