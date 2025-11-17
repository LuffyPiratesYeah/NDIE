"use client";
import { useRouter } from "next/navigation";
import { KAKAO_BASE } from "@/lib/config";

export default function Signup() {
  const router = useRouter();
  const next = () => {
    router.push("/signupagree");
  }
  return (
    <div className="min-h-screen flex flex-col justify-center items-center bg-gray-50 space-y-6 px-4">
      <button onClick={()=>{
          window.location.href =`${KAKAO_BASE}`
        }} className="cursor-pointer  relative flex w-full max-w-[980px] py-[19px] px-0 justify-center items-center bg-[#FF0] text-black rounded shadow-md">
        <img
          className="absolute left-3 w-[43px] h-[43px]"
          alt="Kakao Icon"
          src="/TALK.svg"
        />
        카카오톡으로 시작하기
      </button>
      <div className="flex items-center justify-center space-x-4 w-full max-w-[1300px]">
        <div className="w-[333px] h-[2px] bg-[#E2E1E1]" />
        <button onClick={()=>{
          window.location.href ="https://observant-agreement-17f.notion.site/20abd5ffe3fa805ca553d136e71891a3?source=copy_link"
        }} className="cursor-pointer text-[18px] font-normal text-[#335CFF] font-['Inter'] whitespace-nowrap">
          회원가입하는 방법 3초만에 알아보기
        </button>
        <div className="w-[333px] h-[2px] bg-[#E2E1E1]" />
      </div>

      <button onClick={next} className="hover:bg-[#ededed] cursor-pointer  relative flex w-full max-w-[980px] py-[19px] px-0 justify-center items-center bg-white text-black rounded shadow-md">
        ID/PW 회원가입
      </button>
      
    </div>
  );
}
