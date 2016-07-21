package com.gank.demo.opengl.glsurfaceview;

/**
 * @author gaofeng
 */

public class GLScript {

    public class Square {
        public static final String VertexShaderCode =
                // This matrix member variable provides a hook to manipulate
                // the coordinates of the objects that use this vertex shader
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        // The matrix must be included as a modifier of gl_Position.
                        // Note that the uMVPMatrix factor *must be first* in order
                        // for the matrix multiplication product to be correct.
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}";

        public static final String FragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";
    }

    public class Triangle {
        public static final String VertexShaderCode =
                // This matrix member variable provides a hook to manipulate
                // the coordinates of the objects that use this vertex shader
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        // The matrix must be included as a modifier of gl_Position.
                        // Note that the uMVPMatrix factor *must be first* in order
                        // for the matrix multiplication product to be correct.
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}";

        public static final String FragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";
    }

    public class Texture1 {
        public static final String VertexShaderCode =
                "attribute vec4 position;" +
                        "attribute vec2 texCoords;" +
                        "varying vec2 outTexCoords;" +
                        "void main() {" +
                        "gl_Position = position;" +
                        "outTexCoords = texCoords;" +
                        "}";

        public static final String FragmentShaderCode =
                "precision lowp float;" +
                        "varying vec2 outTexCoords;" +
                        "uniform sampler2D sampler;" +
                        "void main() {" +
                        "gl_FragColor = texture2D(sampler, outTexCoords);" +
                        "}";
    }

    public class Texture {
        public static final String VertexShaderCode =
                "attribute vec4 texCoords;" +
                        "attribute vec4 position;" +
                        "uniform mat4 projection;" +
                        "uniform mat4 texture;" +
                        "varying vec2 outTexCoords;" +
                        "void main() {" +
                        "gl_Position = projection * position;" +
                        "outTexCoords = (texture * texCoords).st;" +
                        "}";

        public static final String FragmentShaderCode =
                "precision lowp float;" +
                        "const int g_iWeightNumber = 9;" +
                        "const float g_fGene = 0.7;"+
                        "uniform vec2 g_aryVerticalOffset[g_iWeightNumber];" +
                        "uniform float g_aryWeight[g_iWeightNumber]; " +
                        "uniform sampler2D sampler;" +
                        "varying vec2 outTexCoords;" +
                        "void main() {" +
                        "vec4 vec4Sum = vec4(0.0);" +
                        "for(int i = 0; i < g_iWeightNumber; ++i)" +
                        "{" +
                        "vec4Sum += texture2D(sampler, outTexCoords + g_aryVerticalOffset[i])*g_aryWeight[i]*g_fGene;" +
                        "vec4Sum += texture2D(sampler, outTexCoords - g_aryVerticalOffset[i])*g_aryWeight[i]*g_fGene;" +
                        "}" +
                        "gl_FragColor = vec4Sum;" +
                        //"gl_FragColor = texture2D(sampler, outTexCoords);" +
                        "}";
    }
}
