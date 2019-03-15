package com.yoke.utils;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 * @author Michael Danilov
 * @version $Revision$
 */
/**
 * Minimized by Tar van Krieken, to just contain the keycodes
 */

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This class is not supported in Android 1.0. It is merely provided to maintain
 * interface compatibility with desktop Java implementations.
 *
 * @since Android 1.0
 */
public class Keys {
    public static final int KEY_FIRST = 400;
    public static final int KEY_LAST = 402;
    public static final int KEY_TYPED = 400;
    public static final int KEY_PRESSED = 401;
    public static final int KEY_RELEASED = 402;
    public static final int VK_ENTER = 10;
    public static final int VK_BACK_SPACE = 8;
    public static final int VK_TAB = 9;
    public static final int VK_CANCEL = 3;
    public static final int VK_CLEAR = 12;
    public static final int VK_SHIFT = 16;
    public static final int VK_CONTROL = 17;
    public static final int VK_ALT = 18;
    public static final int VK_PAUSE = 19;
    public static final int VK_CAPS_LOCK = 20;
    public static final int VK_ESCAPE = 27;
    public static final int VK_SPACE = 32;
    public static final int VK_PAGE_UP = 33;
    public static final int VK_PAGE_DOWN = 34;
    public static final int VK_END = 35;
    public static final int VK_HOME = 36;
    public static final int VK_LEFT = 37;
    public static final int VK_UP = 38;
    public static final int VK_RIGHT = 39;
    public static final int VK_DOWN = 40;
    public static final int VK_COMMA = 44;
    public static final int VK_MINUS = 45;
    public static final int VK_PERIOD = 46;
    public static final int VK_SLASH = 47;
    public static final int VK_0 = 48;
    public static final int VK_1 = 49;
    public static final int VK_2 = 50;
    public static final int VK_3 = 51;
    public static final int VK_4 = 52;
    public static final int VK_5 = 53;
    public static final int VK_6 = 54;
    public static final int VK_7 = 55;
    public static final int VK_8 = 56;
    public static final int VK_9 = 57;
    public static final int VK_SEMICOLON = 59;
    public static final int VK_EQUALS = 61;
    public static final int VK_A = 65;
    public static final int VK_B = 66;
    public static final int VK_C = 67;
    public static final int VK_D = 68;
    public static final int VK_E = 69;
    public static final int VK_F = 70;
    public static final int VK_G = 71;
    public static final int VK_H = 72;
    public static final int VK_I = 73;
    public static final int VK_J = 74;
    public static final int VK_K = 75;
    public static final int VK_L = 76;
    public static final int VK_M = 77;
    public static final int VK_N = 78;
    public static final int VK_O = 79;
    public static final int VK_P = 80;
    public static final int VK_Q = 81;
    public static final int VK_R = 82;
    public static final int VK_S = 83;
    public static final int VK_T = 84;
    public static final int VK_U = 85;
    public static final int VK_V = 86;
    public static final int VK_W = 87;
    public static final int VK_X = 88;
    public static final int VK_Y = 89;
    public static final int VK_Z = 90;
    public static final int VK_OPEN_BRACKET = 91;
    public static final int VK_BACK_SLASH = 92;
    public static final int VK_CLOSE_BRACKET = 93;
    public static final int VK_NUMPAD0 = 96;
    public static final int VK_NUMPAD1 = 97;
    public static final int VK_NUMPAD2 = 98;
    public static final int VK_NUMPAD3 = 99;
    public static final int VK_NUMPAD4 = 100;
    public static final int VK_NUMPAD5 = 101;
    public static final int VK_NUMPAD6 = 102;
    public static final int VK_NUMPAD7 = 103;
    public static final int VK_NUMPAD8 = 104;
    public static final int VK_NUMPAD9 = 105;
    public static final int VK_MULTIPLY = 106;
    public static final int VK_ADD = 107;
    public static final int VK_SEPARATER = 108;
    public static final int VK_SEPARATOR = 108;
    public static final int VK_SUBTRACT = 109;
    public static final int VK_DECIMAL = 110;
    public static final int VK_DIVIDE = 111;
    public static final int VK_DELETE = 127;
    public static final int VK_NUM_LOCK = 144;
    public static final int VK_SCROLL_LOCK = 145;
    public static final int VK_F1 = 112;
    public static final int VK_F2 = 113;
    public static final int VK_F3 = 114;
    public static final int VK_F4 = 115;
    public static final int VK_F5 = 116;
    public static final int VK_F6 = 117;
    public static final int VK_F7 = 118;
    public static final int VK_F8 = 119;
    public static final int VK_F9 = 120;
    public static final int VK_F10 = 121;
    public static final int VK_F11 = 122;
    public static final int VK_F12 = 123;
    public static final int VK_F13 = 61440;
    public static final int VK_F14 = 61441;
    public static final int VK_F15 = 61442;
    public static final int VK_F16 = 61443;
    public static final int VK_F17 = 61444;
    public static final int VK_F18 = 61445;
    public static final int VK_F19 = 61446;
    public static final int VK_F20 = 61447;
    public static final int VK_F21 = 61448;
    public static final int VK_F22 = 61449;
    public static final int VK_F23 = 61450;
    public static final int VK_F24 = 61451;
    public static final int VK_PRINTSCREEN = 154;
    public static final int VK_INSERT = 155;
    public static final int VK_HELP = 156;
    public static final int VK_META = 157;
    public static final int VK_BACK_QUOTE = 192;
    public static final int VK_QUOTE = 222;
    public static final int VK_KP_UP = 224;
    public static final int VK_KP_DOWN = 225;
    public static final int VK_KP_LEFT = 226;
    public static final int VK_KP_RIGHT = 227;
    public static final int VK_DEAD_GRAVE = 128;
    public static final int VK_DEAD_ACUTE = 129;
    public static final int VK_DEAD_CIRCUMFLEX = 130;
    public static final int VK_DEAD_TILDE = 131;
    public static final int VK_DEAD_MACRON = 132;
    public static final int VK_DEAD_BREVE = 133;
    public static final int VK_DEAD_ABOVEDOT = 134;
    public static final int VK_DEAD_DIAERESIS = 135;
    public static final int VK_DEAD_ABOVERING = 136;
    public static final int VK_DEAD_DOUBLEACUTE = 137;
    public static final int VK_DEAD_CARON = 138;
    public static final int VK_DEAD_CEDILLA = 139;
    public static final int VK_DEAD_OGONEK = 140;
    public static final int VK_DEAD_IOTA = 141;
    public static final int VK_DEAD_VOICED_SOUND = 142;
    public static final int VK_DEAD_SEMIVOICED_SOUND = 143;
    public static final int VK_AMPERSAND = 150;
    public static final int VK_ASTERISK = 151;
    public static final int VK_QUOTEDBL = 152;
    public static final int VK_LESS = 153;
    public static final int VK_GREATER = 160;
    public static final int VK_BRACELEFT = 161;
    public static final int VK_BRACERIGHT = 162;
    public static final int VK_AT = 512;
    public static final int VK_COLON = 513;
    public static final int VK_CIRCUMFLEX = 514;
    public static final int VK_DOLLAR = 515;
    public static final int VK_EURO_SIGN = 516;
    public static final int VK_EXCLAMATION_MARK = 517;
    public static final int VK_INVERTED_EXCLAMATION_MARK = 518;
    public static final int VK_LEFT_PARENTHESIS = 519;
    public static final int VK_NUMBER_SIGN = 520;
    public static final int VK_PLUS = 521;
    public static final int VK_RIGHT_PARENTHESIS = 522;
    public static final int VK_UNDERSCORE = 523;
    public static final int VK_FINAL = 24;
    public static final int VK_WINDOWS = 524;
    public static final int VK_CONTEXT_MENU = 525;
    public static final int VK_CONVERT = 28;
    public static final int VK_NONCONVERT = 29;
    public static final int VK_ACCEPT = 30;
    public static final int VK_MODECHANGE = 31;
    public static final int VK_KANA = 21;
    public static final int VK_KANJI = 25;
    public static final int VK_ALPHANUMERIC = 240;
    public static final int VK_KATAKANA = 241;
    public static final int VK_HIRAGANA = 242;
    public static final int VK_FULL_WIDTH = 243;
    public static final int VK_HALF_WIDTH = 244;
    public static final int VK_ROMAN_CHARACTERS = 245;
    public static final int VK_ALL_CANDIDATES = 256;
    public static final int VK_PREVIOUS_CANDIDATE = 257;
    public static final int VK_CODE_INPUT = 258;
    public static final int VK_JAPANESE_KATAKANA = 259;
    public static final int VK_JAPANESE_HIRAGANA = 260;
    public static final int VK_JAPANESE_ROMAN = 261;
    public static final int VK_KANA_LOCK = 262;
    public static final int VK_INPUT_METHOD_ON_OFF = 263;
    public static final int VK_CUT = 65489;
    public static final int VK_COPY = 65485;
    public static final int VK_PASTE = 65487;
    public static final int VK_UNDO = 65483;
    public static final int VK_AGAIN = 65481;
    public static final int VK_FIND = 65488;
    public static final int VK_PROPS = 65482;
    public static final int VK_STOP = 65480;
    public static final int VK_COMPOSE = 65312;
    public static final int VK_ALT_GRAPH = 65406;
    public static final int VK_BEGIN = 65368;
    public static final int VK_UNDEFINED = 0;
    public static final char CHAR_UNDEFINED = (char)(-1);
    public static final int KEY_LOCATION_UNKNOWN = 0;
    public static final int KEY_LOCATION_STANDARD = 1;
    public static final int KEY_LOCATION_LEFT = 2;
    public static final int KEY_LOCATION_RIGHT = 3;
    public static final int KEY_LOCATION_NUMPAD = 4;
    private int keyCode;
    private char keyChar;
    private int keyLocation;
    public static String getKeyText(int keyCode) {
        String[] rawName = getPublicStaticFinalIntFieldName(keyCode); //$NON-NLS-1$
        if ((rawName == null) || (rawName.length == 0)) {
            return ("Unknown keyCode: " + (keyCode >= 0 ? "0x" : "-0x") + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    Integer.toHexString(Math.abs(keyCode)));
        }
        String propertyName = getPropertyName(rawName);
        String defaultName = getDefaultName(rawName);
        return defaultName;
    }
    private static String getDefaultName(String[] rawName) {
        String name = ""; //$NON-NLS-1$
        for (int i = 0; true; i++) {
            String part = rawName[i];
            name += new String(new char[] {part.charAt(0)}).toUpperCase() +
                    part.substring(1).toLowerCase();
            if (i == (rawName.length - 1)) {
                break;
            }
            name += " "; //$NON-NLS-1$
        }
        return name;
    }
    private static String getPropertyName(String[] rawName) {
        String name = rawName[0].toLowerCase();
        for (int i = 1; i < rawName.length; i++) {
            String part = rawName[i];
            name += new String(new char[] {part.charAt(0)}).toUpperCase() +
                    part.substring(1).toLowerCase();
        }
        return ("AWT." + name); //$NON-NLS-1$
    }
    private static String[] getPublicStaticFinalIntFieldName(int value) {
        Field[] allFields = Keys.class.getDeclaredFields();
        try {
            for (Field field : allFields) {
                Class<?> ssalc = field.getType();
                int modifiers = field.getModifiers();
                if (ssalc.isPrimitive() && ssalc.getName().equals("int") && //$NON-NLS-1$
                        Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers) &&
                        Modifier.isStatic(modifiers))
                {
                    if (field.getInt(null) == value){
                        final String name = field.getName();
                        final int prefixLength = name.indexOf("_") + 1;
                        return name.substring(prefixLength).split("_"); //$NON-NLS-1$
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}