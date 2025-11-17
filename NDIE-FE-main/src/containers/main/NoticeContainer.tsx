'use client'

import React, { useEffect, useState } from "react";
import { getAnnouncement } from "@/service/announcement";

type Notice = {
  title: string;
  createdAt: string;
  content: string;
};

export default function NoticeContainer() {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [notices, setNotices] = useState<Notice[]>([]);
  const [isAnimating, setIsAnimating] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const baseStyle = "w-[7.5rem] flex items-center justify-center text-gray-500";

  useEffect(() => {
    const fetchNotices = async () => {
      try {
        setIsLoading(true);
        setError(null);
        const data = await getAnnouncement();
        setNotices(data);
        setIsLoading(false);
      } catch (error) {
        console.error("공지사항 불러오기 실패:", error);
        setError("공지사항을 불러오는데 실패했습니다.");
        setIsLoading(false);
      }
    };
    fetchNotices();
  }, []);

  // 자동 슬라이드
  useEffect(() => {
    if (notices.length === 0) return;

    const interval = setInterval(() => {
      handleSlide("next");
    }, 7500);

    return () => clearInterval(interval);
  }, [notices, currentIndex]);

  const handleSlide = (direction: "prev" | "next") => {
    if (isAnimating) return;
    setIsAnimating(true);

    setTimeout(() => {
      setCurrentIndex((prevIndex) => {
        if (direction === "next") {
          return prevIndex < notices.length - 1 ? prevIndex + 1 : 0;
        } else {
          return prevIndex > 0 ? prevIndex - 1 : notices.length - 1;
        }
      });
      setIsAnimating(false);
    }, 300);
  };

  if (isLoading) {
    return (
      <div className="relative h-[11.25rem] w-full overflow-hidden border-t border-b border-[#EAEAEA] bg-white flex items-center justify-center">
        <p className="text-gray-500">로딩 중...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="relative h-[11.25rem] w-full overflow-hidden border-t border-b border-[#EAEAEA] bg-white flex items-center justify-center">
        <p className="text-red-500">{error}</p>
      </div>
    );
  }

  if (notices.length === 0) {
    return (
      <div className="relative h-[11.25rem] w-full overflow-hidden border-t border-b border-[#EAEAEA] bg-white flex items-center justify-center">
        <p className="text-gray-500">공지사항이 없습니다.</p>
      </div>
    );
  }

  return (
    <div className="relative h-[11.25rem] w-full overflow-hidden border-t border-b border-[#EAEAEA] bg-white">
      <button
        className={`${baseStyle} absolute left-0 top-0 h-full border-r border-[#EAEAEA] text-2xl z-10 disabled:text-gray-300 bg-white`}
        onClick={() => handleSlide("prev")}
        disabled={isAnimating}
      >
        ◀
      </button>
      <div className="w-full h-full overflow-hidden px-[7.5rem]"> {/* 좌우 버튼 공간 확보 */}
        <div
          className="flex h-full transition-transform duration-300 ease-in-out"
          style={{
            width: `${notices.length * 100}%`,
            transform: `translateX(-${(100 / notices.length) * currentIndex}%)`,
          }}
        >
          {notices.map((notice, index) => (
            <div
              key={index}
              className="w-full flex-shrink-0 p-6"
              style={{ width: `${100 / notices.length}%` }}
            >
              <p className="text-sm text-gray-400 mb-1">공지사항</p>
              <div className="flex items-center justify-between mb-2">
                <h3 className="text-lg font-semibold text-black truncate max-w-[80%]">
                  {notice.title}
                </h3>
                <span className="text-xs text-gray-400">
                  {notice.createdAt.slice(0, 10).replaceAll("-", ".")}
                </span>
              </div>
              <p className="text-sm text-gray-600 leading-relaxed line-clamp-3">
                {notice.content}
              </p>
            </div>
          ))}
        </div>
      </div>
      <button
        className={`${baseStyle} absolute right-0 top-0 h-full border-l border-[#EAEAEA] text-2xl z-10 disabled:text-gray-300 bg-white`}
        onClick={() => handleSlide("next")}
        disabled={isAnimating}
      >
        ▶
      </button>
    </div>
  );
}