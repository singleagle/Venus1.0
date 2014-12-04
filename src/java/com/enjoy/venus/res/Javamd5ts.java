package com.enjoy.venus.res;

import java.io.PrintStream;

public class Javamd5ts
{
  public static void main(String[] paramArrayOfString)
  {
    Mademd5 localMademd5 = new Mademd5();
    System.out.println(localMademd5.toMd5("0.0"));
    System.out.println(localMademd5.toMd5(Double.valueOf(0.0D)));
    System.out.println(localMademd5.toMd5(Float.valueOf(0.0F)));
    System.out.println(localMademd5.toMd5(Integer.valueOf(1)));
  }
}

/* Location:           D:\program files\sqlitebrowser_200_b1_win\ExportWeixin_dex2jar.jar
 * Qualified Name:     com.ndktools.javamd5.Javamd5ts
 * JD-Core Version:    0.6.2
 */