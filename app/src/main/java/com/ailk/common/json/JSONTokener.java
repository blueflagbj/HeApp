package com.ailk.common.json;

import com.tencent.tinker.android.p131dx.instruction.Opcodes;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener {
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;

    public JSONTokener(Reader reader2) {
        this.reader = !reader2.markSupported() ? new BufferedReader(reader2) : reader2;
        this.eof = false;
        this.usePrevious = false;
        this.previous = 0;
        this.index = 0;
        this.character = 1;
        this.line = 1;
    }

    public JSONTokener(InputStream inputStream) throws JSONException {
        this((Reader) new InputStreamReader(inputStream));
    }

    public JSONTokener(String s) {
        this((Reader) new StringReader(s));
    }

    public void back() throws JSONException {
        if (this.usePrevious || this.index <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.index--;
        this.character--;
        this.usePrevious = true;
        this.eof = false;
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 'W';
    }

    public boolean end() {
        return this.eof && !this.usePrevious;
    }

    public boolean more() throws JSONException {
        next();
        if (end()) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        int c;
        long j = 0;
        if (this.usePrevious) {
            this.usePrevious = false;
            c = this.previous;
        } else {
            try {
                c = this.reader.read();
                if (c <= 0) {
                    this.eof = true;
                    c = 0;
                }
            } catch (IOException exception) {
                throw new JSONException((Throwable) exception);
            }
        }
        this.index++;
        if (this.previous == 13) {
            this.line++;
            if (c != 10) {
                j = 1;
            }
            this.character = j;
        } else if (c == 10) {
            this.line = 1 + this.line;
            this.character = 0;
        } else {
            this.character++;
        }
        this.previous = (char) c;
        return this.previous;
    }

    public char next(char c) throws JSONException {
        char n = next();
        if (n == c) {
            return n;
        }
        throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
    }

    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        char[] chars = new char[n];
        for (int pos = 0; pos < n; pos++) {
            chars[pos] = next();
            if (end()) {
                throw syntaxError("Substring bounds error");
            }
        }
        return new String(chars);
    }

    public char nextClean() throws JSONException {
        char c;
        do {
            c = next();
            if (c == 0) {
                break;
            }
        } while (c <= ' ');
        return c;
    }

    public String nextString(char quote) throws JSONException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = next();
            switch (c) {
                case 0:
                case 10:
                case 13:
                    throw syntaxError("Unterminated string");
                case Opcodes.IPUT_BOOLEAN /*92*/:
                    char c2 = next();
                    switch (c2) {
                        case '\"':
                        case '\'':
                        case '/':
                        case Opcodes.IPUT_BOOLEAN /*92*/:
                            sb.append(c2);
                            break;
                        case Opcodes.SGET_OBJECT /*98*/:
                            sb.append(8);
                            break;
                        case 'f':
                            sb.append(12);
                            break;
                        case 'n':
                            sb.append(10);
                            break;
                        case 'r':
                            sb.append(13);
                            break;
                        case 't':
                            sb.append(9);
                            break;
                        case 'u':
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        default:
                            throw syntaxError("Illegal escape.");
                    }
                default:
                    if (c != quote) {
                        sb.append(c);
                        break;
                    } else {
                        return sb.toString();
                    }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    public String nextTo(char delimiter) throws JSONException {
        char c;
        StringBuilder sb = new StringBuilder();
        for (;;) {
             c = this.next();
            if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001b  */
    public String nextTo(String delimiters) throws JSONException {
        char c;
        StringBuffer sb = new StringBuffer();
        for (;;) {
            c = this.next();
            if (delimiters.indexOf(c) >= 0 || c == 0 ||
                    c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    public Object nextValue() throws JSONException {
        char c = nextClean();
        switch (c) {
            case '\"':
            case '\'':
                return nextString(c);
            case Opcodes.IPUT_OBJECT /*91*/:
                back();
                return new JSONArray(this);
            case '{':
                back();
                return new JSONObject(this);
            default:
                StringBuilder sb = new StringBuilder();
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = next();
                }
                back();
                String string = sb.toString().trim();
                if (!"".equals(string)) {
                    return JSONObject.stringToValue(string);
                }
                throw syntaxError("Missing value");
        }
    }

    public char skipTo(char to) throws JSONException {
        char c;
        try {
            long startIndex = this.index;
            long startCharacter = this.character;
            long startLine = this.line;
            this.reader.mark(1000000);
            while (true) {
                c = next();
                if (c != 0) {
                    if (c == to) {
                        back();
                        break;
                    }
                } else {
                    this.reader.reset();
                    this.index = startIndex;
                    this.character = startCharacter;
                    this.line = startLine;
                    break;
                }
            }
            return c;
        } catch (IOException exception) {
            throw new JSONException((Throwable) exception);
        }
    }

    public JSONException syntaxError(String message) {
        return new JSONException(message + toString());
    }

    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}