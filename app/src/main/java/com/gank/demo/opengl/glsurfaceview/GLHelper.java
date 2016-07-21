package com.gank.demo.opengl.glsurfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


/**
 * @author Gank
 */
public class GLHelper {

    private final static String TAG = "OPENGL";

    public static int initProgram(String vScript, String fScript) {
        int vertexShader = compileScript(GLES20.GL_VERTEX_SHADER, vScript);
        Log.e(TAG, "V: " + vertexShader);
        int fragmentShader = compileScript(GLES20.GL_FRAGMENT_SHADER, fScript);
        Log.e(TAG, "F: " + fragmentShader);
        int program = createAndLinkProgram(vertexShader, fragmentShader);
        Log.e(TAG, "p: " + program);
        if (!checkProgram(program)) {
            program = 0;
        }
        return program;
    }

    private static int FLOAT_SIZE = 4;
    private static int SHORT_SIZE = 2;

    public static FloatBuffer floatArrayToBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                array.length * FLOAT_SIZE);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }

    public static ShortBuffer shotArrayToBuffer(short[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                array.length * SHORT_SIZE);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer buffer = bb.asShortBuffer();
        buffer.put(array);
        buffer.position(0);
        return buffer;
    }

    public static int loadTexture(Context context, String name) {
        int[] textureId = new int[1];
        // Generate a texture object
        GLES20.glGenTextures(1, textureId, 0);
        if (textureId[0] != 0) {
            Bitmap bitmap = null;
            InputStream is = null;
            try {
                is = context.getResources().getAssets().open(name);
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error loading Bitmap.");
                }
            }
// Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
// Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE);
// Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
// Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        } else {
            throw new RuntimeException("Error loading texture.");
        }
        return textureId[0];
    }

    private static int createAndLinkProgram(int vertexsharder, int fragmentsharder) {
        int programId = GLES20.glCreateProgram();//创建一个程序
        if (programId == 0) {
            Log.e(TAG, "Error Create Link Program");
            return 0;
        }
        GLES20.glAttachShader(programId, vertexsharder); //和着色器进行关联
        GLES20.glAttachShader(programId, fragmentsharder);//和着色器进行关联
        GLES20.glLinkProgram(programId); //把program链接起来
        int status[] = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, status, 0); //这地方一样是检查是否有错误发生。
        if (status[0] == 0) {
            Log.e(TAG, "link status is error.");
            GLES20.glDeleteProgram(programId);
            return 0;
        }
        return programId;

    }

    private static boolean checkProgram(int programId) {
        GLES20.glValidateProgram(programId);
        int status[] = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, status, 0);
        if (status[0] == 0) {
            Log.e(TAG, "program is error");
            return false;
        }
        return true;
    }

    private static int compileScript(int type, String script) {
        int shader = GLES20.glCreateShader(type); //创建一个着色器对象,TYPE表示顶点着色器和片段着色器
        if (shader == 0) { //0表示有错误
            return 0;
        }
        GLES20.glShaderSource(shader, script); //把脚本代码传给OpenGL 引擎
        GLES20.glCompileShader(shader); //开始编译
        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0); //看看编译结果是否有错误。
        if (status[0] == 0) {
            GLES20.glDeleteShader(shader);//有错误我们删除这个对象。
            Log.e(TAG, "Error Compile Script:" + script);
            return 0;
        }
        return shader;
    }
}
