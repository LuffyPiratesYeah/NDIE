"use client";
import Logo from "@public/images/logo.svg";
import { useState } from "react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/store/useAuthStore";
import { API_BASE, KAKAO_BASE } from "@/lib/config";


export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();
  const setToken = useAuthStore((state) => state.setToken);


  const handleLogin = async () => {
  try {
    const res = await fetch(`${API_BASE}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({ email, password }),
    });
    const authHeader = res.headers.get('Authorization');
    const text = await res.text(); 
    let data;

    try { 
      data = JSON.parse(text); 
    } catch {
      data = null; 
    }

    if (!res.ok) {
      const errorMessage = data?.message || '로그인 실패';
      throw new Error(errorMessage);
    }

    if (authHeader) {
      const token = authHeader.startsWith('Bearer ') ? authHeader.slice(7) : authHeader; //쿠키 로컬스토리지에 저장
      localStorage.setItem('token', token);
      setToken(token);
    } else if (data?.token) {
      localStorage.setItem('token', data.token);
      setToken(data.token);
    }

    router.push('/');
  } catch (error) {
    console.error('로그인 중 오류:', error);
    const errorMessage = error instanceof Error ? error.message : '로그인 중 오류 발생';
    alert(errorMessage);
  }
};


  return (
    <div className="min-h-screen flex items-center justify-center bg-white">
      <div className="w-full max-w-sm text-center space-y-6">
        <div className="flex justify-center mt--80">
          <Image src={Logo} alt={"Logo"} width={90} height={90} />
        </div>

        <button className="relative flex cursor-pointer justify-center items-center w-full bg-[#FF0] text-black font-bold py-2 rounded" onClick={()=>{
          window.location.href =`${KAKAO_BASE}`
        }}>
          <img className="absolute left-3 w-[30px] h-[30px]" alt="Kakao Icon" src="/TALK.svg" />
          카카오톡으로 시작하기
        </button>

        <div className="flex items-center my-4">
          <div className="flex-grow border-t border-gray-300" />
          <span className="mx-4 text-gray-500 text-sm">또는</span>
          <div className="flex-grow border-t border-gray-300" />
        </div>

        <input
          type="email"
          placeholder="이메일"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full border border-gray-300 rounded px-4 py-2 mb-2"
          onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
        />
        <input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full border border-gray-300 rounded px-4 py-2"
          onKeyDown={(e) => e.key === 'Enter' && handleLogin()}
        />

        <button
          onClick={handleLogin}
          className="hover:bg-[#f78000] cursor-pointer w-full bg-orange-400 text-white font-bold py-2 rounded mt-4"
        >
          로그인
        </button>

        <div className="flex justify-between items-center text-sm text-gray-600 mt-2">
          <label className="flex items-center">
            <input type="checkbox" className="cursor-pointer mr-2" />
            로그인 상태 유지
          </label>
          <div className="space-x-3">
            <button className="hover:underline cursor-pointer">비밀번호 재설정</button>
            <button onClick={() => router.push('/signup')} className="hover:underline cursor-pointer">
              회원가입
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
