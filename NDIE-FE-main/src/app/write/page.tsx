"use client";
import { Suspense } from 'react';
import React, {useRef, useState} from "react";
import ContentInputScreen from "@/containers/write/ContentInputScreen";
import ContentOutputScreen from "@/containers/write/ContentOutputScreen";
import WriteFooter from "@/containers/write/WriteFooter";
import WriteModalScreen from "@/containers/write/ModalScreen";
import FileInput from "@/containers/write/FileInput";
import Loading from "@/components/ui/loading";
import {useLoadingStore} from "@/store/loading";

export default function Write() {
  const [title,setTitle] = useState("");
  const [content, setContent] = useState("");
  const [selectedOption, setSelectedOption] = useState("");
  const fileRef = useRef<HTMLInputElement | null>(null);
  const {isLoading} = useLoadingStore();

  const contentRef = useRef<HTMLTextAreaElement>(null);

  const addText = (num: string, position : number)=>{
    if(content.length > 0) setContent((prevText) =>prevText + "\n" + num);
    else setContent((prevText) =>prevText + num);
    setTimeout(()=>{
      if(content.length){
        contentRef.current?.focus();
        contentRef.current?.setSelectionRange(position, position);
      }
      else{
        contentRef.current?.focus();
        contentRef.current?.setSelectionRange(position-1, position-1);
      }
    }, 0)
  }
  return (
    <div className=" flex items-center justify-center h-[90.5vh] w-[100vw] font-[family-name:var(--font-geist-sans)]">
      <Suspense fallback={<Loading/>}>
        <ContentInputScreen
          fileRef={fileRef}
          title={title}
          setTitle={setTitle}
          setContent={setContent}
          setSelectedOption={setSelectedOption}
          content={content}
          selectedOption={selectedOption}
          addText={addText}
          contentRef={contentRef}
        />
      </Suspense>

      <ContentOutputScreen title={title} content={content}  />
      <WriteFooter  title={title} content={content} selectedOption={selectedOption} />
      <FileInput addText={addText} content={content} fileRef={fileRef} />
      <Suspense fallback={<Loading/>}>
        <WriteModalScreen title={title} content={content} />
      </Suspense>
      {isLoading && <Loading />}
    </div>
  );
}