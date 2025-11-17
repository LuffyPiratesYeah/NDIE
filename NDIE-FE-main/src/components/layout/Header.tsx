"use client";
import Link from "next/link";
import Image from "next/image";
import Logo from "@public/images/logo.svg";
import { useAuthStore } from "@/store/useAuthStore";
import { useEffect, useState } from "react";

export const Header = () => {
  const token = useAuthStore((state) => state.token);
  const setToken = useAuthStore((state) => state.setToken);
  const [hydrated, setHydrated] = useState(false);

  useEffect(() => {
    setHydrated(true);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    setToken(null);
  };
  if (!hydrated) return null; 
  
  return (
    <header className="h-[4.6875rem] sticky top-0 left-0 right-0 border-b-[#585858] border-b-[0.4px] pl-40 pr-40 flex justify-between items-center z-50 bg-white">
      <div className="flex gap-20 items-center">
        <div className="flex gap-4 items-center">
          <Image src={Logo} alt={"Logo"} />
          <p className="text-2xl">NDIE</p>
        </div>
        <Link href="/">홈</Link>
        <Link href="/act">활동</Link>
        <Link href="/qna">QnA</Link>
        <Link href="/announcement">공지사항</Link>
        {token && <Link href="/write">게시물 작성</Link>}
      </div>
      <div className="flex gap-11 items-center">
        {token ? (
          <button onClick={handleLogout} className="text-black">로그아웃</button>
        ) : (
          <>
            <Link href="/login">로그인</Link>
            <Link href="/signup">회원가입</Link>
          </>
        )}
      </div>
    </header>
  )
}