#include <jni.h>
#include <string>
#include <math.h>
#include <stdlib.h>
#include <android/bitmap.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_kuyuzhiqi_imageblur_LibraryLoadUtil_stringFromJNI2(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ test";
    return env->NewStringUTF(hello.c_str());
}

#define PI 3.14

void gaussBlur1(int *pix, int w, int h, int radius) {
    float sigma = (float) (1.0 * radius / 2.57);
    float deno = (float) (1.0 / (sigma * sqrt(2.0 * PI)));
    float nume = (float) (-1.0 / (2.0 * sigma * sigma));
    float *gaussMatrix = (float *) malloc(sizeof(float) * (radius + radius + 1));
    float gaussSum = 0.0;
    for (int i = 0, x = -radius; x <= radius; ++x, ++i) {
        float g = (float) (deno * exp(1.0 * nume * x * x));
        gaussMatrix[i] = g;
        gaussSum += g;
    }
    int len = radius + radius + 1;
    for (int i = 0; i < len; ++i)
        gaussMatrix[i] /= gaussSum;
    int *rowData = (int *) malloc(w * sizeof(int));
    int *listData = (int *) malloc(h * sizeof(int));
    for (int y = 0; y < h; ++y) {
        memcpy(rowData, pix + y * w, sizeof(int) * w);
        for (int x = 0; x < w; ++x) {
            float r = 0, g = 0, b = 0;
            gaussSum = 0;
            for (int i = -radius; i <= radius; ++i) {
                int k = x + i;
                if (0 <= k && k <= w) {
                    //得到像素点的rgb值
                    int color = rowData[k];
                    int cr = (color & 0x00ff0000) >> 16;
                    int cg = (color & 0x0000ff00) >> 8;
                    int cb = (color & 0x000000ff);
                    r += cr * gaussMatrix[i + radius];
                    g += cg * gaussMatrix[i + radius];
                    b += cb * gaussMatrix[i + radius];
                    gaussSum += gaussMatrix[i + radius];
                }
            }
            int cr = (int) (r / gaussSum);
            int cg = (int) (g / gaussSum);
            int cb = (int) (b / gaussSum);
            pix[y * w + x] = cr << 16 | cg << 8 | cb | 0xff000000;
        }
    }
    for (int x = 0; x < w; ++x) {
        for (int y = 0; y < h; ++y)
            listData[y] = pix[y * w + x];
        for (int y = 0; y < h; ++y) {
            float r = 0, g = 0, b = 0;
            gaussSum = 0;
            for (int j = -radius; j <= radius; ++j) {
                int k = y + j;
                if (0 <= k && k <= h) {
                    int color = listData[k];
                    int cr = (color & 0x00ff0000) >> 16;
                    int cg = (color & 0x0000ff00) >> 8;
                    int cb = (color & 0x000000ff);
                    r += cr * gaussMatrix[j + radius];
                    g += cg * gaussMatrix[j + radius];
                    b += cb * gaussMatrix[j + radius];
                    gaussSum += gaussMatrix[j + radius];
                }
            }
            int cr = (int) (r / gaussSum);
            int cg = (int) (g / gaussSum);
            int cb = (int) (b / gaussSum);
            pix[y * w + x] = cr << 16 | cg << 8 | cb | 0xff000000;
        }
    }
    free(gaussMatrix);
    free(rowData);
    free(listData);
}

//高斯模糊
extern "C"
JNIEXPORT void JNICALL
Java_com_kuyuzhiqi_imageblur_LibraryLoadUtil_gaussBlur(JNIEnv *env, jobject instance, jobject bitmap) {
    AndroidBitmapInfo info = {0};
    int *data = NULL;
    AndroidBitmap_getInfo(env, bitmap, &info);
    AndroidBitmap_lockPixels(env, bitmap, (void **) &data);
    gaussBlur1(data, info.width, info.height, 20);
    AndroidBitmap_unlockPixels(env, bitmap);
}

//灰度图
extern "C"
JNIEXPORT jintArray JNICALL
Java_com_kuyuzhiqi_imageblur_LibraryLoadUtil_imgToGray(JNIEnv *env, jobject instance, jintArray buf, jint w, jint h) {
    jint *cbuf;
    //传false会报cannot initialize a parameter of type 'jboolean *' (aka 'unsigned char *')
    cbuf = env->GetIntArrayElements(buf, JNI_FALSE);
    if (cbuf == NULL) {
        return 0;
    }
    int alpha = 0xFF << 24;
    for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
            //获取像素颜色
            int color = cbuf[w * i + j];
            int red = ((color & 0x00FF0000) >> 16);
            int green = ((color & 0x0000FF00) >> 8);
            int blue = color & 0x000000FF;
            color = (red + green + blue) / 3;
            color = alpha | (color << 16) | (color << 8) | color;
            cbuf[w * i + j] = color;
        }
    }
    int size = w * h;
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, cbuf);
    env->ReleaseIntArrayElements(buf, cbuf, 0);
    return result;
}