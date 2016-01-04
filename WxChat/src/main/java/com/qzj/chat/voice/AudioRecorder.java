package com.qzj.chat.voice;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.qzj.chat.uitls.MD5;

public class AudioRecorder
{
	private static int SAMPLE_RATE_IN_HZ = 8000;
	private static String DEFAULT_DIR = "/QzjChat/voice";

	final MediaRecorder recorder = new MediaRecorder();
	final String path;

	public AudioRecorder() {
		this.path = sanitizePath(getVoiceFileName());
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/"))
		{
			path = "/" + path;
		}
		if (!path.contains("."))
		{
			path += ".amr";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath() + DEFAULT_DIR + path;
	}

	public void start() throws IOException {
		String state = Environment.getExternalStorageState();
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			throw new IOException("SD Card is not mounted,It is  " + state + ".");
		}
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created");
		}
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
	}

	public void stop() throws IOException {
		recorder.stop();
		recorder.release();
	}
	
	public double getAmplitude() {		
		if (recorder != null){			
			return  (recorder.getMaxAmplitude());		
		} else
			return 0;	
	}

	private String getVoiceFileName(){
		SimpleDateFormat s = new SimpleDateFormat("yyyyMMddhhmmss");
		String format = s.format(new Date());
		return MD5.getMD5("chat_voice_"+format);
	}

	public String getPath() {
		return path;
	}
}