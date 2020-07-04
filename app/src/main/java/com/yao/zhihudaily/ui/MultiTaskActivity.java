package com.yao.zhihudaily.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yao.zhihudaily.R;
import com.yao.zhihudaily.model.UploadImageItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Function7;
import io.reactivex.schedulers.Schedulers;

public class MultiTaskActivity extends BaseActivity {

    private static final String TAG = "MultiTaskActivity";

    public static final int CONCURRENCY_TASK_NUMBER = 5;//并发任务数量

    public static final int MAX_UPLOAD_IMAGE = 7;

    public final UploadImageItem EMPTY_ITEM = new UploadImageItem();

    @BindView(R.id.button1)
    Button mButton1;
    @BindView(R.id.button2)
    Button mButton2;
    @BindView(R.id.button3)
    Button mButton3;
    @BindView(R.id.button4)
    Button mButton4;

    private ExecutorService mExecutorService = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_task);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1://使用计数器确定进度
                List<UploadImageItem> toUploadImageList1 = generateImageList(5);
                button1(toUploadImageList1);
                break;
            case R.id.button2://使用CountDownLatch确定进度
                List<UploadImageItem> toUploadImageList2 = generateImageList(5);
                button2(toUploadImageList2);
                break;
            case R.id.button3://使用线程池和Callable
                List<UploadImageItem> toUploadImageList3 = generateImageList(5);
                button3(toUploadImageList3);
                break;
            case R.id.button4://使用zip的基本操作
                List<UploadImageItem> toUploadImageList4 = generateImageList(5);
                button4(toUploadImageList4);
                break;
            case R.id.button5://任务可变的，使用zip操作符，通过if和else确定任务数量的方法
                List<UploadImageItem> toUploadImageList5 = generateImageList(5);
                button5(toUploadImageList5);
                break;
            case R.id.button6://任务可变的，正确使用zip操作符的方法
                List<UploadImageItem> toUploadImageList6 = generateImageList(5);
                button6(toUploadImageList6);
                break;
            case R.id.button7:
//                button7(toUploadImageList3);
                break;
            case R.id.button8:
//                button8(toUploadImageList4);
                break;
        }
    }

    private List<UploadImageItem> generateImageList(int count) {
        List<UploadImageItem> uploadImageItemList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UploadImageItem uploadImageItem = new UploadImageItem("image_file_path" + i, System.currentTimeMillis(), "22.54337925643233,113.91730280173233");
            uploadImageItemList.add(uploadImageItem);
        }
        return uploadImageItemList;
    }

    /**
     * 使用计数器确定进度
     */
    private void button1(List<UploadImageItem> uploadImageItemList) {
        final int[] finishCount = {0};
        for (UploadImageItem uploadImageItem : uploadImageItemList) {
            Observable.just(uploadImageItem)//这里应该写成网络请求
                    .subscribeOn(Schedulers.io())
                    .map(response -> {
                        SystemClock.sleep(new Random().nextInt(1000) + 1000);
                        uploadImageItem.urlPath = "url path from " + uploadImageItem.filePath;
                        Log.e(TAG, "MultiTaskActivity.java - uploadImageFile() finish! item.filePath:" + uploadImageItem.filePath);
                        return uploadImageItem;
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<UploadImageItem>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(UploadImageItem uploadImageItem) {
                            finishCount[0]++;
                            if (finishCount[0] == uploadImageItemList.size()) {
                                Log.e(TAG, "MultiTaskActivity.java - run() ----- 全部任务完成了");
                                for (UploadImageItem output : uploadImageItemList) {
                                    Log.e(TAG, "MultiTaskActivity.java - run() ----- 打印实体:" + output);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    /**
     * 使用CountDownLatch确定进度
     */
    private void button2(List<UploadImageItem> uploadImageItemList) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                CountDownLatch countDownLatch = new CountDownLatch(CONCURRENCY_TASK_NUMBER);

                for (UploadImageItem uploadImageItem : uploadImageItemList) {
                    Observable.just(uploadImageItem)//这里应该写成网络请求
                            .subscribeOn(Schedulers.io())
                            .map(response -> {
                                SystemClock.sleep(new Random().nextInt(1000) + 1000);
                                uploadImageItem.urlPath = "url path from " + uploadImageItem.filePath;
                                Log.e(TAG, "MultiTaskActivity.java - uploadImageFile() finish! item.filePath:" + uploadImageItem.filePath);
                                return uploadImageItem;
                            })
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<UploadImageItem>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(UploadImageItem uploadImageItem) {
                                    countDownLatch.countDown();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "MultiTaskActivity.java - run() ----- 全部任务完成了");
                for (UploadImageItem output : uploadImageItemList) {
                    Log.e(TAG, "MultiTaskActivity.java - run() ----- 打印实体:" + output);
                }
            }
        });
    }

    /**
     * 使用线程池和Callable，IO任务应该放到Callable里面同步返回。
     */
    private void button3(List<UploadImageItem> uploadImageItemList) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Future<UploadImageItem>> futureList = new ArrayList<>();

                for (UploadImageItem uploadImageItem : uploadImageItemList) {
                    Future<UploadImageItem> future = mExecutorService.submit(new IoCallable(uploadImageItem));
                    futureList.add(future);
                }

                List<UploadImageItem> uploadImageItems = new ArrayList<>();
                try {
                    for (Future<UploadImageItem> future : futureList) {
                        UploadImageItem uploadImageItem = future.get();
                        uploadImageItems.add(uploadImageItem);
                        Log.e(TAG, String.format("MultiTaskActivity.java - run() ----- 等到了任务%s完成", uploadImageItem.filePath));
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "MultiTaskActivity.java - run() ----- 全部任务完成了");

                for (UploadImageItem uploadImageItem : uploadImageItems) {
                    Log.e(TAG, "MultiTaskActivity.java - run() ----- 打印实体:" + uploadImageItem);
                }
            }
        });
    }

    /**
     * 使用zip的基本操作
     */
    private void button4(List<UploadImageItem> uploadImageItemList) {
        Observable<UploadImageItem> observable0;
        Observable<UploadImageItem> observable1;
        Observable<UploadImageItem> observable2;
        Observable<UploadImageItem> observable3;
        Observable<UploadImageItem> observable4;

        observable0 = uploadImageFile(uploadImageItemList.get(0));
        observable1 = uploadImageFile(uploadImageItemList.get(1));
        observable2 = uploadImageFile(uploadImageItemList.get(2));
        observable3 = uploadImageFile(uploadImageItemList.get(3));
        observable4 = uploadImageFile(uploadImageItemList.get(4));

        Observable.zip(observable0, observable1, observable2, observable3, observable4,
                new Function5<UploadImageItem, UploadImageItem, UploadImageItem, UploadImageItem, UploadImageItem, List<UploadImageItem>>() {
                    @Override
                    public List<UploadImageItem> apply(UploadImageItem uploadImageItem, UploadImageItem uploadImageItem2, UploadImageItem uploadImageItem3, UploadImageItem uploadImageItem4, UploadImageItem uploadImageItem5) throws Exception {
                        return uploadImageItemList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UploadImageItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<UploadImageItem> uploadImageItems) {
                        for (UploadImageItem uploadImageItem : uploadImageItems) {
                            Log.e(TAG, "MultiTaskActivity.java - run() ----- 打印实体:" + uploadImageItem);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 任务可变的，使用zip操作符，通过if和else确定任务数量的方法
     */
    private void button5(List<UploadImageItem> uploadImageItemList) {

        Observable<UploadImageItem> observable0;
        Observable<UploadImageItem> observable1;
        Observable<UploadImageItem> observable2;
        Observable<UploadImageItem> observable3;
        Observable<UploadImageItem> observable4;

        Observable<List<UploadImageItem>> observableUploadResult;

        //使用zip操作符并发上传图片。可以很好的保证上传图片后的排列顺序问题
        //根据uploadImageItemList有多少个图片上传请求，使用对应的zip重载参数方法。
        observable0 = uploadImageFile(uploadImageItemList.get(0));
        if (uploadImageItemList.size() > 1) {
            observable1 = uploadImageFile(uploadImageItemList.get(1));
            if (uploadImageItemList.size() > 2) {
                observable2 = uploadImageFile(uploadImageItemList.get(2));
                if (uploadImageItemList.size() > 3) {
                    observable3 = uploadImageFile(uploadImageItemList.get(3));
                    if (uploadImageItemList.size() > 4) {
                        observable4 = uploadImageFile(uploadImageItemList.get(4));

                        //同时上传5张图片的情况
                        observableUploadResult = Observable.zip(observable0, observable1, observable2, observable3, observable4, new Function5<UploadImageItem, UploadImageItem, UploadImageItem, UploadImageItem, UploadImageItem, List<UploadImageItem>>() {
                            @Override
                            public List<UploadImageItem> apply(UploadImageItem uploadGoodsImageItem0, UploadImageItem uploadGoodsImageItem1, UploadImageItem uploadGoodsImageItem2, UploadImageItem uploadGoodsImageItem3, UploadImageItem uploadGoodsImageItem4) throws Exception {
                                return uploadImageItemList;
                            }
                        });
                    } else {

                        //同时上传4张图片的情况
                        observableUploadResult = Observable.zip(observable0, observable1, observable2, observable3, new Function4<UploadImageItem, UploadImageItem, UploadImageItem, UploadImageItem, List<UploadImageItem>>() {
                            @Override
                            public List<UploadImageItem> apply(UploadImageItem uploadGoodsImageItem0, UploadImageItem uploadGoodsImageItem1, UploadImageItem uploadGoodsImageItem2, UploadImageItem uploadGoodsImageItem3) throws Exception {
                                return uploadImageItemList;
                            }
                        });
                    }
                } else {

                    //同时上传3张图片的情况
                    observableUploadResult = Observable.zip(observable0, observable1, observable2, new Function3<UploadImageItem, UploadImageItem, UploadImageItem, List<UploadImageItem>>() {
                        @Override
                        public List<UploadImageItem> apply(UploadImageItem uploadGoodsImageItem0, UploadImageItem uploadGoodsImageItem1, UploadImageItem uploadGoodsImageItem2) throws Exception {
                            return uploadImageItemList;
                        }
                    });
                }
            } else {

                //同时上传2张图片的情况
                observableUploadResult = Observable.zip(observable0, observable1, new BiFunction<UploadImageItem, UploadImageItem, List<UploadImageItem>>() {
                    @Override
                    public List<UploadImageItem> apply(UploadImageItem uploadGoodsImageItem0, UploadImageItem uploadGoodsImageItem1) throws Exception {
                        return uploadImageItemList;
                    }
                });
            }
        } else {

            //上传1张图片的情况
            observableUploadResult = observable0.map(new Function<UploadImageItem, List<UploadImageItem>>() {
                @Override
                public List<UploadImageItem> apply(UploadImageItem uploadGoodsImageItem1) throws Exception {
                    return uploadImageItemList;
                }
            });
        }

        //并发上传图片请求后的结果处理。
        //如果任一张图片上传失败，会走到onError方法。
        //如果都成功了，会调用 mRootView.uploadImageSuccess 走下一个流程。
        observableUploadResult
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UploadImageItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<UploadImageItem> uploadImageItems) {
                        for (UploadImageItem uploadImageItem : uploadImageItems) {
                            Log.e(TAG, "MultiTaskActivity.java - run() ----- 打印实体:" + uploadImageItem);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 任务可变的，正确使用zip操作符的方法
     */
    private void button6(List<UploadImageItem> uploadImageItemList) {

        Observable<UploadImageItem> observable0;
        Observable<UploadImageItem> observable1;
        Observable<UploadImageItem> observable2;
        Observable<UploadImageItem> observable3;
        Observable<UploadImageItem> observable4;
        Observable<UploadImageItem> observable5;
        Observable<UploadImageItem> observable6;

        List<UploadImageItem> originUploadImageList = new ArrayList<>(uploadImageItemList);
        //添加null，补全到装货最多图片数量
        int validCount = originUploadImageList.size();//原始数量值
        for (int i = 0; i < MAX_UPLOAD_IMAGE - validCount; i++) {
            //伪装操作1：添加值为null的item。
            originUploadImageList.add(null);
        }

        observable0 = uploadImageFileV2(originUploadImageList.get(0));
        observable1 = uploadImageFileV2(originUploadImageList.get(1));
        observable2 = uploadImageFileV2(originUploadImageList.get(2));
        observable3 = uploadImageFileV2(originUploadImageList.get(3));
        observable4 = uploadImageFileV2(originUploadImageList.get(4));
        observable5 = uploadImageFileV2(originUploadImageList.get(5));
        observable6 = uploadImageFileV2(originUploadImageList.get(6));

        Observable<List<UploadImageItem>> observableUploadResult;

        //使用zip操作符并发上传图片。并且可以很好的保证上传图片后的排列顺序问题。
        Observable
                .zip(observable0, observable1, observable2, observable3, observable4, observable5, observable6,
                        new Function7<UploadImageItem,
                                UploadImageItem,
                                UploadImageItem,
                                UploadImageItem,
                                UploadImageItem,
                                UploadImageItem,
                                UploadImageItem,
                                List<UploadImageItem>>() {
                            @Override
                            public List<UploadImageItem> apply(UploadImageItem uploadImageItem0,
                                                               UploadImageItem uploadImageItem1,
                                                               UploadImageItem uploadImageItem2,
                                                               UploadImageItem uploadImageItem3,
                                                               UploadImageItem uploadImageItem4,
                                                               UploadImageItem uploadImageItem5,
                                                               UploadImageItem uploadImageItem6) {

                                List<UploadImageItem> resultUploadImageItemList = new ArrayList<>();
                                resultUploadImageItemList.add(uploadImageItem0);
                                resultUploadImageItemList.add(uploadImageItem1);
                                resultUploadImageItemList.add(uploadImageItem2);
                                resultUploadImageItemList.add(uploadImageItem3);
                                resultUploadImageItemList.add(uploadImageItem4);
                                resultUploadImageItemList.add(uploadImageItem5);
                                resultUploadImageItemList.add(uploadImageItem6);
                                //伪装操作3：识别出EMPTY_ITEM，过滤掉。
                                while (resultUploadImageItemList.contains(EMPTY_ITEM)) {
                                    resultUploadImageItemList.remove(EMPTY_ITEM);
                                }

                                return resultUploadImageItemList;
                            }
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UploadImageItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<UploadImageItem> uploadImageItems) {
                        for (UploadImageItem uploadImageItem : uploadImageItems) {
                            Log.e(TAG, "MultiTaskActivity.java - run() ----- 打印实体:" + uploadImageItem);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // ------------------------------

    public Observable<UploadImageItem> uploadImageFileInCurrentThread(UploadImageItem item) {
        return Observable.just(item)//这里应该写成网络请求
                .map(response -> {
                    SystemClock.sleep(new Random().nextInt(1000) + 1000);
                    item.urlPath = "url path from " + item.filePath;
                    Log.e(TAG, "MultiTaskActivity.java - uploadImageFile() finish! item.filePath:" + item.filePath);
                    return item;
                });
    }

    public Observable<UploadImageItem> uploadImageFile(UploadImageItem item) {
        return Observable.just(item)//这里应该写成网络请求
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    SystemClock.sleep(new Random().nextInt(1000) + 1000);
                    item.urlPath = "url path from " + item.filePath;
                    Log.e(TAG, "MultiTaskActivity.java - uploadImageFile() finish! item.filePath:" + item.filePath);
                    return item;
                });
    }

    public Observable<UploadImageItem> uploadImageFileV2(UploadImageItem item) {
        //伪装操作2：如果是null的item，不需要执行网络请求，而是发送一个EMPTY_ITEM。
        if (item == null) {
            return Observable.just(EMPTY_ITEM);
        }

        //图片已经上传过，已拥有网络路径
        if (item.urlPath != null) {
            return Observable.just(item);
        }

        return Observable.just(item)//这里应该写成网络请求
                .subscribeOn(Schedulers.io())
                .map(response -> {
                    SystemClock.sleep(new Random().nextInt(1000) + 1000);
                    item.urlPath = "url path from " + item.filePath;
                    Log.e(TAG, "MultiTaskActivity.java - uploadImageFile() finish! item.filePath:" + item.filePath);
                    return item;
                });
    }

    private static class IoCallable implements Callable<UploadImageItem> {

        public UploadImageItem mUploadImageItem;

        public IoCallable(UploadImageItem uploadImageItem) {
            this.mUploadImageItem = uploadImageItem;
        }

        @Override
        public UploadImageItem call() throws Exception {
            SystemClock.sleep(new Random().nextInt(1000) + 1000);
            mUploadImageItem.urlPath = "url path from " + mUploadImageItem.filePath;
            Log.e(TAG, "IoRunnable.java - run() ----- finish! item.filePath::" + mUploadImageItem.filePath);
            return mUploadImageItem;
        }
    }

    interface Callback {
        void onCallback();
    }
}
