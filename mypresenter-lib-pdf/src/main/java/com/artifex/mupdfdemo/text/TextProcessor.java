package com.artifex.mupdfdemo.text;

public interface TextProcessor {
    void onStartLine();
    void onWord(TextWord word);
    void onEndLine();
}
