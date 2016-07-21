package com.gank.demo.opengl.glsurfaceview;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by GankLi on 2016/7/18.
 */

public class Texture {

    private final static int COORDS_POS_VERTEX = 3;
    private final static int COORDS_TEX_VERTEX = 2;

    private static final float[] Full_SquareCoords = new float[]{
            -1f, 1f, 0.0f, // Position 0
            -1f, -1f, 0.0f, // Position 1
            1f, -1f, 0.0f, // Position 2
            1f, 1f, 0.0f, // Position 3
    };

    private static final float[] Texture_SquareCoords = new float[]{
            0, 0f, // TexCoord 0
            1.0f, 0f, // TexCoord 3
            1.0f, 1, // TexCoord 2
            0, 1, // TexCoord 1
    };

    private static final short[] DrawOrder = new short[]{0, 1, 2, 2, 3, 0};

    private static final FloatBuffer VertexBuffer = GLHelper.floatArrayToBuffer(Full_SquareCoords);
    private static final FloatBuffer TextureBuffer = GLHelper.floatArrayToBuffer(Texture_SquareCoords);
    private static final ShortBuffer DrawListBuffer = GLHelper.shotArrayToBuffer(DrawOrder);

    private Context mContext;
    private final int mProgram;
    private int attrPosition;
    private int attrTexCoords;
    private int uniformProjection;
    private int uniformTexture;
    private int uniformSampler;

    private int uniformAryWeight;
    private int uniformAryVerticalOffset;

    private int mTextureId;

    private static final float SUM = 137;// 137;
    private static final float Blur = 90f;

    private static final float AryWeight[] = {12/ SUM,16/ SUM,12/ SUM,16/ SUM,25/ SUM,16/ SUM,12/ SUM,16/ SUM,12/ SUM};
    private static final float VerticalOffset[] = {-1/Blur,-1/Blur, 0,-1/Blur, 1/Blur,-1/Blur
            -1/Blur, 0, 0, 0, 1/Blur, 0
            -1/Blur, 1/Blur, 0, 1/Blur, 1/Blur, 1/Blur};
    private static final FloatBuffer AryWeightBuffer = GLHelper.floatArrayToBuffer(AryWeight);
    private static final FloatBuffer VerticalOffsetBuffer = GLHelper.floatArrayToBuffer(VerticalOffset);

    public Texture(Context context) {
        mContext = context;

        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        mProgram = GLHelper.initProgram(GLScript.Texture.VertexShaderCode, GLScript.Texture.FragmentShaderCode);

        attrPosition = GLES20.glGetAttribLocation(mProgram, "position");
        attrTexCoords = GLES20.glGetAttribLocation(mProgram, "texCoords");
        uniformProjection = GLES20.glGetUniformLocation(mProgram, "projection");
        uniformTexture = GLES20.glGetUniformLocation(mProgram, "texture");
        uniformSampler = GLES20.glGetUniformLocation(mProgram, "sampler");
        uniformAryWeight = GLES20.glGetUniformLocation(mProgram, "g_aryWeight");
        uniformAryVerticalOffset = GLES20.glGetUniformLocation(mProgram, "g_aryVerticalOffset");
        mTextureId = GLHelper.loadTexture(mContext, "test.jpg");
    }

    public void draw(float[] projectionMatrix, float[] textureMatrix) {
        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(attrPosition);
        GLES20.glEnableVertexAttribArray(attrTexCoords);
        // Set the sampler to texture unit 0
        GLES20.glUniform1i(uniformSampler, 0);

        GLES20.glUniformMatrix4fv(uniformProjection, 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(uniformTexture, 1, false, textureMatrix, 0);

        GLES20.glUniform1fv(uniformAryWeight,AryWeight.length, AryWeightBuffer);
        GLES20.glUniform2fv(uniformAryVerticalOffset,AryWeight.length, VerticalOffsetBuffer);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        // load the position
        VertexBuffer.position(0);
        GLES20.glVertexAttribPointer(attrPosition,
                COORDS_POS_VERTEX, GLES20.GL_FLOAT,
                false, Full_SquareCoords.length, VertexBuffer);
        // load the texture coordinate
        VertexBuffer.position(0);
        GLES20.glVertexAttribPointer(attrTexCoords,
                COORDS_TEX_VERTEX, GLES20.GL_FLOAT,
                false, Texture_SquareCoords.length, TextureBuffer);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, DrawOrder.length, GLES20.GL_UNSIGNED_SHORT,
                DrawListBuffer);

        // Disable VertexBuffer array
        GLES20.glDisableVertexAttribArray(attrPosition);
        GLES20.glDisableVertexAttribArray(attrTexCoords);
    }

    static class Matrix {
        public final static float[] Projection = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
        public final static float[] Projection_TEST = {0.5f, 0, 0, 0, // 0.5 -- X轴缩放0.5
                0, 0.5f, 0, 0, // 0.5 -- 轴缩放0.5
                0, 0, 1, 0,
                0, 0, 0, 1};   // 0.5 -- 整体放大2倍

        public final static float[] Texture_Normal = {-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1};
        public final static float[] Texture_90 = {0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1};
        public final static float[] Texture_180 = {1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1};
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("OPENGL", glOperation + ": glError " + error);
//            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
