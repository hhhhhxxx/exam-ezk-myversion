package com.jdpu.examsystem.face.service;


import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.jdpu.examsystem.face.entity.ProcessInfo;
import com.jdpu.examsystem.face.entity.UserCompareInfo;
import com.jdpu.examsystem.face.util.UserRamCache;


import java.util.List;


public interface FaceEngineService {

    List<FaceInfo> detectFaces(ImageInfo imageInfo);

    Float compareFace(ImageInfo imageInfo1,ImageInfo imageInfo2) ;

    byte[] extractFaceFeature(ImageInfo imageInfo,FaceInfo faceInfo);

    List<UserCompareInfo> faceRecognition(byte[] faceFeature, List<UserRamCache.UserInfo> userInfoList, float passRate) ;

    List<ProcessInfo> process(ImageInfo imageInfo, List<FaceInfo> faceInfoList);
}
