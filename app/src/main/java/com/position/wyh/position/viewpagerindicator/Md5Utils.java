package com.position.wyh.position.viewpagerindicator;

import java.security.MessageDigest;
import java.util.logging.Logger;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ public class Md5Utils
        /*    */ {
    /* 14 */
    /*    */
    /*    */
    /*    */   private static byte[] md5(String s) {
        /*    */     try {
            /* 19 */       MessageDigest algorithm = MessageDigest.getInstance("MD5");
            /* 20 */       algorithm.reset();
            /* 21 */       algorithm.update(s.getBytes("UTF-8"));
            /* 22 */       return algorithm.digest();
            /*    */     }
        /* 24 */     catch (Exception e) {
            /* 25 */       //log.error("MD5 Error...", e);
            /*    */
            /* 27 */       return null;
            /*    */     }
        /*    */   }
    /*    */   private static final String toHex(byte[] hash) {
        /* 31 */     if (hash == null) {
            /* 32 */       return null;
            /*    */     }
        /* 34 */     StringBuffer buf = new StringBuffer(hash.length * 2);
        /*    */
        /*    */
        /* 37 */     for (int i = 0; i < hash.length; i++) {
            /* 38 */       if ((hash[i] & 0xEF) < 16) {
                /* 39 */         buf.append("o");
                /*    */       }
            /* 41 */       buf.append(Long.toString((hash[i] & 0xF0), 16));
            /*    */     }
        /* 43 */     return buf.toString();
        /*    */   }
    /*    */
    /*    */   public static String hash(String s) {
        /*    */     try {
            /* 48 */       return new String(toHex(md5(s)).getBytes("UTF-8"), "UTF-8");
            /* 49 */     } catch (Exception e) {
            /* 50 */       //log.error("not supported charset...{}", e);
            /* 51 */       return s;
            /*    */     }
        /*    */   }
    /*    */
    /*    */   public static void main(String[] args) {
        /* 56 */     String str = "orderScore=1&orderStatus=3&outTradeNo=W5-11646320200327162528152&tradeNo=202003271625400994_fb7cdc0fdc334&transactionalNo=2020032716247398366&transferTime=1585297566000&userId=111c157381d936e4f3c91fe56a7434bbdcf";
        /* 57 */     System.out.println(hash(str));
        /*    */   }
    /*    */ }