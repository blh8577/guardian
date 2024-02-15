package com.dependent.guardian.service.function;

import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Service
public class ImageUploaderService {
    public String saveImage(HttpServletRequest request, String path) throws ServletException, IOException {
        //쓰는 경로는 매핑안됨 무조건 진짜 경로로 해야함
//        String savePath = "/home/vcap/images/"+path;
        String savePath = "C:\\test\\"+path;
        String contentType = request.getContentType();

        File uploadDir = new File(savePath);
        if (!uploadDir.exists()){
            //폴더 존재하지 않으면 생성
            uploadDir.mkdir();
        }

        //form 의 enctype이 multipart인지 검사
        if (contentType != null &&  contentType.toLowerCase().startsWith("multipart/")) {
            // getParts()를 통해 Body에 넘어온 데이터들을 각각의  Part로 쪼개어 리턴
            Collection<Part> parts = request.getParts();

            for (Part part : parts) {
                //해당 part가 파일인지 검사
                if  (part.getHeader("Content-Disposition").contains("filename=")) {

                    /* 아래 if문 부터는 테스트를 해보지 않았음 */
                    //name값에 해당하는지
                    // Img를 붙이는 이유는 Worker 객체는 문자열경로만 받기때문에 매핑되면 MissMatch 에러가 나기때문에
                    // name 값으로 된 폴더에 넣기위해 실용성 좋게 만들었음
                    if(part.getName().equals(path+"Img")){
                        //업로드 할 파일명을 추출
                        String fileName =  extractFileName(part.getHeader("Content-Disposition"));
                        if (part.getSize() > 0) {
                            //파일명이 겹치지 않게 파일명에 랜덤 문자를 붙임
                            fileName = randomString() + fileName;
                            //파일 쓰기
                            part.write(savePath + File.separator + fileName);
                            //파일 이름(+경로)을 리스트에 저장
                            part.delete();
                            return "/images/"+path+"/"+ fileName;
                        }
                    }
                }
            }

        }
        return null;
    }

    private String randomString(){
        String str = "";
        str+=System.currentTimeMillis();
        str += (char)((int)(Math.random()*26)+65);
        str += (char)((int)(Math.random()*26)+97);

        return str;
    }

    private String extractFileName(String partHeader) {
        for (String cd : partHeader.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf("=") +  1).trim().replace("\"", "");;
                int index = fileName.lastIndexOf(File.separator);
                return fileName.substring(index + 1);
            }
        }
        return null;
    }
}
