package com.u8.sdk.utils;


public final class Base64 {
    private static final int BASELENGTH = 128;
    private static final int LOOKUPLENGTH = 64;
    private static final int TWENTYFOURBITGROUP = 24;
    private static final int EIGHTBIT = 8;
    private static final int SIXTEENBIT = 16;
    private static final int FOURBYTE = 4;
    private static final int SIGN = -128;
    private static final char PAD = '=';
    private static final boolean fDebug = false;
    private static final byte[] base64Alphabet = new byte[128];
    private static final char[] lookUpBase64Alphabet = new char[64];

    static {
        for (int i = 0; i < BASELENGTH; ++i) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;

        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';
    }


    private static boolean isWhiteSpace(char octect) {
        return (octect == ' ' || octect == '\r' || octect == '\n' || octect == '\t');
    }


    private static boolean isPad(char octect) {
        return (octect == '=');
    }


    private static boolean isData(char octect) {
        return (octect < '' && base64Alphabet[octect] != -1);
    }


    public static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * 8;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        int numberQuartet = (fewerThan24bits != 0) ? (numberTriplets + 1) : numberTriplets;
        char[] encodedData = null;

        encodedData = new char[numberQuartet * 4];

        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;


        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];


            l = (byte) (b2 & 0xF);
            k = (byte) (b1 & 0x3);

            byte val1 = ((b1 & 0xFFFFFF80) == 0) ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xC0);
            byte val2 = ((b2 & 0xFFFFFF80) == 0) ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 0xF0);
            byte val3 = ((b3 & 0xFFFFFF80) == 0) ? (byte) (b3 >> 6) : (byte) (b3 >> 6 ^ 0xFC);


            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2 | val3];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3F];
        }


        if (fewerThan24bits == 8) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x3);


            byte val1 = ((b1 & 0xFFFFFF80) == 0) ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xC0);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = '=';
            encodedData[encodedIndex++] = '=';
        } else if (fewerThan24bits == 16) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0xF);
            k = (byte) (b1 & 0x3);

            byte val1 = ((b1 & 0xFFFFFF80) == 0) ? (byte) (b1 >> 2) : (byte) (b1 >> 2 ^ 0xC0);
            byte val2 = ((b2 & 0xFFFFFF80) == 0) ? (byte) (b2 >> 4) : (byte) (b2 >> 4 ^ 0xF0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = '=';
        }

        return new String(encodedData);
    }


    public static String decodeString(String s) {
        try {
            byte[] buf = decode(s);

            s = new String(buf, "UTF-8");
            s = s.replaceAll("[\\n|\\r]", "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }


    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();

        int len = removeWhiteSpace(base64Data);

        if (len % 4 != 0) {
            return null;
        }

        int numberQuadruple = len / 4;

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte[] decodedData = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = Character.MIN_VALUE, d2 = Character.MIN_VALUE, d3 = Character.MIN_VALUE, d4 = Character.MIN_VALUE;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[numberQuadruple * 3];

        for (; i < numberQuadruple - 1; i++) {

            if (!isData(d1 = base64Data[dataIndex++]) || !isData(d2 = base64Data[dataIndex++]) ||
                    !isData(d3 = base64Data[dataIndex++]) ||
                    !isData(d4 = base64Data[dataIndex++])) {
                return null;
            }

            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) ((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData(d1 = base64Data[dataIndex++]) || !isData(d2 = base64Data[dataIndex++])) {
            return null;
        }

        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData(d3) || !isData(d4)) {
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xF) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            }
            if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0) {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) ((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
                return tmp;
            }
            return null;
        }

        b3 = base64Alphabet[d3];
        b4 = base64Alphabet[d4];
        decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
        decodedData[encodedIndex++] = (byte) ((b2 & 0xF) << 4 | b3 >> 2 & 0xF);
        decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);


        return decodedData;
    }


    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }


        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }
}


