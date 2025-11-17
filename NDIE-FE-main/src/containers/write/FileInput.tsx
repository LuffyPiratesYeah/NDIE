import React from "react";
import {uploadImg} from "@/app/api/activity";

export default function FileInput({
  fileRef,
  content,
  addText
}: {
  fileRef: React.RefObject<HTMLInputElement | null>;
  content : string
  addText : (num : string, position : number)=>void
                                  }){
  const addImgText = async (img : File) =>{
    if(!fileRef.current) return;
    fileRef.current.value = "";
    if(!img) {
      alert("이미지 파일만 업로드할 수 있습니다. (jpg, png, jpeg)");
      return ;
    }
    const data = new FormData();
    data.append('file', img);

    const src = await uploadImg(data);
    if(src){
      addText(`<이미지 src="${src.url}"></이미지>`, content.length);
    }else{
      alert("img 등록에 실패")
    }
  }

  return (
    <input ref={fileRef} type={"file"} style={{display: "none"}} onChange={(e)=> {
      if(!e.target.files) return
      addImgText(e.target.files[0])
    }} />

  )
}