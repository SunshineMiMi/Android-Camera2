package net.lqtest.app;

import java.io.IOException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements Callback {

	// private Button mButton;

	/**********************/
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera = null;
	private Camera.Parameters mParameters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);
	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open(0);//后置摄像头
		try {
			// 设置预览监听
			mCamera.setPreviewDisplay(holder);
			Camera.Parameters parameters = mCamera.getParameters();

			if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				parameters.set("orientation", "portrait");
				mCamera.setDisplayOrientation(90);
				parameters.setRotation(90);
			} else {
				parameters.set("orientation", "landscape");
				mCamera.setDisplayOrientation(0);
				parameters.setRotation(0);
			}
			mCamera.setParameters(parameters);
			// 启动摄像头预览
			mCamera.startPreview();
			System.out.println("camera.startpreview");

		} catch (IOException e) {
			e.printStackTrace();
			mCamera.release();
			System.out.println("camera.release");
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mCamera.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (success) {
					initCamera();// 实现相机的参数初始化
					camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
				}
			}

		});
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
		}
	}

	// 相机参数的初始化设置
	private void initCamera() {
		mParameters = mCamera.getParameters();
		mParameters.setPictureFormat(PixelFormat.JPEG);
		mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
		setDispaly(mParameters, mCamera);
		mCamera.setParameters(mParameters);
		mCamera.startPreview();
		mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

	}

	// 控制图像的正确显示方向
	private void setDispaly(Camera.Parameters parameters, Camera camera) {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			setDisplayOrientation(camera, 90);
		} else {
			parameters.setRotation(90);
		}

	}

	// 实现的图像的正确显示
	private void setDisplayOrientation(Camera camera, int i) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null) {
				downPolymorphic.invoke(camera, new Object[] { i });
			}
		} catch (Exception e) {
			Log.e("Came_e", "图像出错");
		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (requestCode == 1) {
	// Toast.makeText(getBaseContext(), "获取OK！", Toast.LENGTH_SHORT).show();
	// }
	// }

}
