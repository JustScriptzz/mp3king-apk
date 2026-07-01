package app.mp3king.android;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.BridgeWebViewClient;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BridgeActivity {

    // File extensions that should be served as real static files
    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
        ".js", ".css", ".png", ".jpg", ".jpeg", ".svg", ".ico",
        ".woff", ".woff2", ".ttf", ".json", ".webp", ".gif", ".mp3", ".ogg"
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Override the WebViewClient to handle SPA routing:
        // any request that doesn't match a real static file gets served app.html
        getBridge().setWebViewClient(new BridgeWebViewClient(getBridge()) {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                String path = request.getUrl().getPath();

                // Let Capacitor handle actual static file requests
                if (path != null) {
                    for (String ext : STATIC_EXTENSIONS) {
                        if (path.endsWith(ext)) {
                            return super.shouldInterceptRequest(view, request);
                        }
                    }
                    // Also pass through the root and app.html itself
                    if (path.equals("/") || path.equals("/app.html") || path.equals("/index.html")) {
                        return super.shouldInterceptRequest(view, request);
                    }
                }

                // For all other paths (SPA routes like /queue, /search, /now-playing, etc.)
                // serve app.html so React Router can handle the route client-side
                try {
                    InputStream is = getApplicationContext().getAssets().open("public/app.html");
                    return new WebResourceResponse("text/html", "UTF-8", is);
                } catch (Exception e) {
                    return super.shouldInterceptRequest(view, request);
                }
            }
        });
    }
}
