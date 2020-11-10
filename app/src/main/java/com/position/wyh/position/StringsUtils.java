package com.position.wyh.position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StringsUtils
        /*     */
        /*     */ {
    /*     */   private static final String NULLSTR = "";
    /*     */   private static final char SEPARATOR = '_';
    /*     */
    /*  31 */   public static <T> T nvl(T value, T defaultValue) { return (value != null) ? value : defaultValue; }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*  41 */   public static boolean isEmpty(Collection<?> coll) { return (isNull(coll) || coll.isEmpty()); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*  51 */   public static boolean isNotEmpty(Collection<?> coll) { return !isEmpty(coll); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*  61 */   public static boolean isEmpty(Object[] objects) { return (isNull(objects) || objects.length == 0); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*  71 */   public static boolean isNotEmpty(Object[] objects) { return !isEmpty(objects); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*  81 */   public static boolean isEmpty(Map<?, ?> map) { return (isNull(map) || map.isEmpty()); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*  91 */   public static boolean isNotEmpty(Map<?, ?> map) { return !isEmpty(map); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /* 101 */   public static boolean isEmpty(String str) { return (isNull(str) || "".equals(str.trim())); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /* 111 */   public static boolean isNotEmpty(String str) { return !isEmpty(str); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /* 121 */   public static boolean isNull(Object object) { return (object == null); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /* 131 */   public static boolean isNotNull(Object object) { return !isNull(object); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /* 141 */   public static boolean isArray(Object object) { return (isNotNull(object) && object.getClass().isArray()); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /* 148 */   public static String trim(String str) { return (str == null) ? "" : str.trim(); }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   public static String substring(String str, int start) {
        /* 159 */     if (str == null) {
            /* 160 */       return "";
            /*     */     }
        /*     */
        /* 163 */     if (start < 0) {
            /* 164 */       start = str.length() + start;
            /*     */     }
        /*     */
        /* 167 */     if (start < 0) {
            /* 168 */       start = 0;
            /*     */     }
        /* 170 */     if (start > str.length()) {
            /* 171 */       return "";
            /*     */     }
        /*     */
        /* 174 */     return str.substring(start);
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   public static String substring(String str, int start, int end) {
        /* 186 */     if (str == null) {
            /* 187 */       return "";
            /*     */     }
        /*     */
        /* 190 */     if (end < 0) {
            /* 191 */       end = str.length() + end;
            /*     */     }
        /* 193 */     if (start < 0) {
            /* 194 */       start = str.length() + start;
            /*     */     }
        /*     */
        /* 197 */     if (end > str.length()) {
            /* 198 */       end = str.length();
            /*     */     }
        /*     */
        /* 201 */     if (start > end) {
            /* 202 */       return "";
            /*     */     }
        /*     */
        /* 205 */     if (start < 0) {
            /* 206 */       start = 0;
            /*     */     }
        /* 208 */     if (end < 0) {
            /* 209 */       end = 0;
            /*     */     }
        /*     */
        /* 212 */     return str.substring(start, end);
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     *
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   public static String toUnderScoreCase(String str) {
        /* 239 */     if (str == null) {
            /* 240 */       return null;
            /*     */     }
        /* 242 */     StringBuilder sb = new StringBuilder();
        /*     */
        /* 244 */     boolean preCharIsUpperCase = true;
        /*     */
        /* 246 */     boolean curreCharIsUpperCase = true;
        /*     */
        /* 248 */     boolean nexteCharIsUpperCase = true;
        /* 249 */     for (int i = 0; i < str.length(); i++) {
            /* 250 */       char c = str.charAt(i);
            /* 251 */       if (i > 0) {
                /* 252 */         preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
                /*     */       } else {
                /* 254 */         preCharIsUpperCase = false;
                /*     */       }
            /*     */
            /* 257 */       curreCharIsUpperCase = Character.isUpperCase(c);
            /*     */
            /* 259 */       if (i < str.length() - 1) {
                /* 260 */         nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
                /*     */       }
            /*     */
            /* 263 */       if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                /* 264 */         sb.append('_');
                /* 265 */       } else if (i != 0 && !preCharIsUpperCase && curreCharIsUpperCase) {
                /* 266 */         sb.append('_');
                /*     */       }
            /* 268 */       sb.append(Character.toLowerCase(c));
            /*     */     }
        /* 270 */     return sb.toString();
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   public static boolean inStringIgnoreCase(String str, String... strs) {
        /* 281 */     if (str != null && strs != null) {
            /* 282 */       for (String s : strs) {
                /* 283 */         if (str.equalsIgnoreCase(trim(s))) {
                    /* 284 */           return true;
                    /*     */         }
                /*     */       }
            /*     */     }
        /* 288 */     return false;
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   public static String convertToCamelCase(String name) {
        /* 298 */     StringBuilder result = new StringBuilder();
        /*     */
        /* 300 */     if (name == null || name.isEmpty())
            /*     */     {
            /* 302 */       return ""; }
        /* 303 */     if (!name.contains("_"))
            /*     */     {
            /* 305 */       return name.substring(0, 1).toUpperCase() + name.substring(1);
            /*     */     }
        /*     */
        /* 308 */     String[] camels = name.split("_");
        /* 309 */     for (String camel : camels) {
            /*     */
            /* 311 */       if (!camel.isEmpty()) {
                /*     */
                /*     */
                /*     */
                /* 315 */         result.append(camel.substring(0, 1).toUpperCase());
                /* 316 */         result.append(camel.substring(1).toLowerCase());
                /*     */       }
            /* 318 */     }  return result.toString();
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   public static String toCamelCase(String s) {
        /* 326 */     if (s == null) {
            /* 327 */       return null;
            /*     */     }
        /* 329 */     s = s.toLowerCase();
        /* 330 */     StringBuilder sb = new StringBuilder(s.length());
        /* 331 */     boolean upperCase = false;
        /* 332 */     for (int i = 0; i < s.length(); i++) {
            /* 333 */       char c = s.charAt(i);
            /*     */
            /* 335 */       if (c == '_') {
                /* 336 */         upperCase = true;
                /* 337 */       } else if (upperCase) {
                /* 338 */         sb.append(Character.toUpperCase(c));
                /* 339 */         upperCase = false;
                /*     */       } else {
                /* 341 */         sb.append(c);
                /*     */       }
            /*     */     }
        /* 344 */     return sb.toString();
        /*     */   }
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */
    /*     */   public static final String sortMapByKeyAsc(Map<String, ?> paramValues) {
        /* 355 */     List<String> keys = new ArrayList<String>(paramValues.keySet());
        /* 356 */     Collections.sort(keys);
        /* 357 */     String prestr = "";
        /* 358 */     for (int i = 0; i < keys.size(); i++) {
            /* 359 */       String key = (String)keys.get(i);
            /* 360 */       if (paramValues.get(key) == null || "".equals(paramValues.get(key))) {
                /*     */         continue;
                /*     */       }
            /* 363 */       String value = paramValues.get(key).toString();
            /* 364 */
            /* 370 */       if (!"".equals(value.trim()))
                /*     */       {
                /*     */
                /* 373 */         prestr = prestr + key + "=" + value + "&"; }  continue;
            /*     */     }
        /* 375 */     if (keys.size() > 0) {
            /* 376 */       prestr = prestr.substring(0, prestr.length() - 1);
            /*     */     }
        /* 378 */     return prestr;
        /*     */   }
    /*     */ }


