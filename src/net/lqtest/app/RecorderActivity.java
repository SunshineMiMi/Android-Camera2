package net.lqtest.app;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import net.lqtest.app.recorder.Recorder;
import net.lqtest.app.recorder.Recorder.OnStateChangedListener;

public class RecorderActivity extends Activity implements OnClickListener, OnStateChangedListener {

	private Button mBtnStartRecorder;
	private Button mBtnStartPlay;
	private Button mBtnStopRecorder;
	private Recorder mRecorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);

		mBtnStartRecorder = (Button) findViewById(R.id.btn_startrecorder);
		mBtnStartPlay = (Button) findViewById(R.id.btn_startplay);
		mBtnStopRecorder = (Button) findViewById(R.id.btn_stoprecorder);

		mBtnStartRecorder.setOnClickListener(this);
		mBtnStartPlay.setOnClickListener(this);
		mBtnStopRecorder.setOnClickListener(this);
		mRecorder = new Recorder();
		mRecorder.setOnStateChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_startrecorder:
				mRecorder.startRecording(MediaRecorder.OutputFormat.THREE_GPP, ".aac", this);
				break;
			case R.id.btn_startplay:
				mRecorder.startPlayback();
				break;
			case R.id.btn_stoprecorder:
				mRecorder.stop();
				mRecorder.delete();
				break;
		}
	}

	@Override
	public void onStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int error) {
		// TODO Auto-generated method stub
		
	}

}
