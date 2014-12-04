package com.enjoy.venus.res;

import com.enjoy.venus.res.MD5;

public class Mademd5
{
  private static String _md5(String paramString)
  {
    return new MD5().getMD5ofStr(paramString);
  }

  private static String doubleTo(Double paramDouble)
  {
    return _md5(paramDouble.toString());
  }

  private static String floatTo(Float paramFloat)
  {
    return _md5(paramFloat.toString());
  }

  private static String intTo(Integer paramInteger)
  {
    return _md5(paramInteger.toString());
  }

  private static String longTo(Long paramLong)
  {
    return _md5(paramLong.toString());
  }

  private static String strTO(String paramString)
  {
    return _md5(paramString);
  }

  private static String strTo(String paramString1, String paramString2)
  {
    return _md5((paramString1 + paramString2).toString());
  }

  public String toMd5(Double paramDouble)
  {
    return doubleTo(paramDouble);
  }

  public String toMd5(Float paramFloat)
  {
    return floatTo(paramFloat);
  }

  public String toMd5(Integer paramInteger)
  {
    return intTo(paramInteger);
  }

  public String toMd5(Long paramLong)
  {
    return longTo(paramLong);
  }

  public String toMd5(String paramString)
  {
    return strTO(paramString);
  }

  public String toMd5(String paramString1, String paramString2)
  {
    return strTo(paramString1, paramString2);
  }
}

/* Location:           D:\program files\sqlitebrowser_200_b1_win\ExportWeixin_dex2jar.jar
 * Qualified Name:     com.ndktools.javamd5.Mademd5
 * JD-Core Version:    0.6.2
 */