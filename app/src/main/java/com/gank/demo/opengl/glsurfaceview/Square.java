package com.gank.demo.opengl.glsurfaceview;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Square {

    // number of coordinates per vertex in this array
    private final static int COORDS_PER_VERTEX = 3;
    private final static float SquareCoords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f}; // top right

    private final static short DrawOrder[] = {0, 1, 2, 0, 3, 2}; // order to draw vertices

    private final static int VertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private final static float Color[] = {1f, 0, 0, 1.0f};


    private static final FloatBuffer VertexBuffer = GLHelper.floatArrayToBuffer(SquareCoords);
    private final ShortBuffer DrawListBuffer = GLHelper.shotArrayToBuffer(DrawOrder);
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Square() {
        // prepare shaders and OpenGL program
        mProgram = GLHelper.initProgram(GLScript.Square.VertexShaderCode, GLScript.Square.FragmentShaderCode);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     *                  this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // Enable vertex array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Vertex
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VertexStride, VertexBuffer);

        // Color
        GLES20.glUniform4fv(mColorHandle, 1, Color, 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, DrawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, DrawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}