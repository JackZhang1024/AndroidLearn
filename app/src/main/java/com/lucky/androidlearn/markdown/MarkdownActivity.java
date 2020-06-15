package com.lucky.androidlearn.markdown;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lucky.androidlearn.R;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;

public class MarkdownActivity extends AppCompatActivity {

    MarkdownView mMarkdownView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);
        mMarkdownView = (MarkdownView)findViewById(R.id.markdown_view);
        mMarkdownView.addStyleSheet(new Github());
        //mMarkdownView.loadMarkdown("**MarkdownView**");
        mMarkdownView.loadMarkdownFromAsset("Java类和接口.md");
        //mMarkdownView.loadMarkdownFromFile(new File());
        //mMarkdownView.loadMarkdownFromUrl("url");
    }


}
