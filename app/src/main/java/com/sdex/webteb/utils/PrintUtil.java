package com.sdex.webteb.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.webkit.WebView;

import java.io.FileNotFoundException;

/**
 * Created by Yuriy Mysochenko on 17.03.2015.
 */
public class PrintUtil {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void printHTML(Context context, String jobName, WebView webView) {
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    public static void printPhoto(Context context, String jobName, Uri image) throws FileNotFoundException {
        PrintHelper photoPrinter = new PrintHelper(context);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap(jobName, image);
    }

}
