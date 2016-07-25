package com.gank.demo.opengl.glsurfaceview;

/**
 * @author Gank
 */

public class GLScript {

    public class Square {
        public static final String VertexShaderCode =
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
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
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
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
                        "attribute vec2 texCoords;" + //纹理坐标
                        "varying vec2 outTexCoords;" + //顶点坐标
                        "void main() {" +
                        "gl_Position = position;" + //设置内置变量
                        "outTexCoords = texCoords;" + //设置纹理位置并传给 FragmentShader
                        "}";

        public static final String FragmentShaderCode =
                "precision lowp float;" +
                        "varying vec2 outTexCoords;" + //接收 VertexShader 的参数
                        "uniform sampler2D sampler;" + //纹理采样
                        "void main() {" +
                        "gl_FragColor = texture2D(sampler, outTexCoords);" + //根据位置（outTexCoords）获取纹理上的颜色， 设置像素的颜色
                        "}";
    }

    public class Texture {
        public static final String VertexShaderCode =
                "attribute vec4 texCoords;" +  //纹理坐标
                        "attribute vec4 position;" + //顶点坐标
                        "uniform mat4 projection;" + //顶点矩阵 做旋转
                        "uniform mat4 texture;" +     //纹理矩阵 做旋转
                        "varying vec2 outTexCoords;" +
                        "void main() {" +
                        "gl_Position = projection * position;" +
                        "outTexCoords = (texture * texCoords).st;" +
                        "}";

        public static final String FragmentShaderCode =
                "precision lowp float;" +
                        "const int g_iWeightNumber = 9;" +
                        "const float g_fGene = 0.7;" +
                        "uniform vec2 g_aryVerticalOffset[g_iWeightNumber];" + //采样的位置便宜
                        "uniform float g_aryWeight[g_iWeightNumber]; " + //每个点的权重
                        "uniform sampler2D sampler;" +
                        "varying vec2 outTexCoords;" +
                        "void main() {" +
                        "vec4 vec4Sum = vec4(0.0);" +
                        "for(int i = 0; i < g_iWeightNumber; ++i)" +
                        "{" +
                        "vec4Sum += texture2D(sampler, outTexCoords + g_aryVerticalOffset[i])*g_aryWeight[i]*g_fGene;" + // X轴模糊
                        "vec4Sum += texture2D(sampler, outTexCoords - g_aryVerticalOffset[i])*g_aryWeight[i]*g_fGene;" + // Y轴模糊
                        "}" +
                        "gl_FragColor = vec4Sum;" +
                        "}";
    }
}
