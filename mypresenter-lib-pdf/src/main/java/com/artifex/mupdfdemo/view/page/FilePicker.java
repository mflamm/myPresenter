package com.artifex.mupdfdemo.view.page;

import android.net.Uri;

public abstract class FilePicker {
	private final FilePickerSupport support;

	FilePicker(FilePickerSupport _support) {
		support = _support;
	}

	public void pick() {
		support.performPickFor(this);
	}

	public abstract void onPick(Uri uri);
}
