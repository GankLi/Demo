package com.gank.demo.opengl.egl;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnTouchListener;

public class OpenGlDemo extends Activity implements Callback, Runnable, OnTouchListener {
	private SurfaceView view;
	private boolean rendering = false;
	private final Object renderLock = new Object();
	private GL10 gl;
	private float red = 0.2f, green = 0.3f, blue = 0.8f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		view = new SurfaceView(this);
		view.getHolder().addCallback(this);
		view.setOnTouchListener(this);
		setContentView(view);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		synchronized (renderLock) {
			Log.d("OpenGlDemo >>>", "Start rendering...");
			rendering = true;
			new Thread(this).start();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		synchronized (renderLock) {
			rendering = false;
		}
	}

	public void run() {
		EGL10 egl = (EGL10) EGLContext.getEGL();
		EGLDisplay disp = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
		egl.eglInitialize(disp, new int[2]);
		EGLConfig[] configs = new EGLConfig[1];
		egl.eglChooseConfig(disp, new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE }, configs, 1, new int[1]);
		EGLConfig config = configs[0];
		// Create surface and bind with native windowing
		EGLSurface surf = egl.eglCreateWindowSurface(disp, config, view.getHolder(), null);
		// Bind with OpenGL context
		EGLContext contx = egl.eglCreateContext(disp, config, EGL10.EGL_NO_CONTEXT, null);
		egl.eglMakeCurrent(disp, surf, surf, contx);
		gl = (GL10) contx.getGL();
		while (true) {
			synchronized (renderLock) {
				if (!rendering) {
					break;
				}
			}
			render(gl);
			egl.eglSwapBuffers(disp, surf);
		}
		Log.d("OpenGlDemo >>>", "Stop rendering");
		// Finalize
		egl.eglMakeCurrent(disp, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
		egl.eglDestroyContext(disp, contx);
		egl.eglDestroySurface(disp, surf);
		gl = null;
	}

	private void render(GL10 gl) {
		gl.glClearColor(red, green, blue, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	public boolean onTouch(View view, MotionEvent e) {
		red = e.getX() / view.getWidth();
		green = e.getY() / view.getHeight();
		blue = 1.0f;
		return true;
	}
}