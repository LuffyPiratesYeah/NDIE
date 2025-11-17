import React from "react";

export default function HomeBanner() {
  return (
    <div className={`absolute top-1/2 -translate-y-1/2 z-1 bg-white/50 h-[40rem] left-[10rem] right-[10rem] 
                     rounded-4xl flex flex-col justify-center items-center text-center px-4 overflow-hidden`}>
      <div className="z-10">
        <h1 className="text-2xl sm:text-3xl md:text-4xl font-semibold mb-2 flex items-end justify-center gap-2">
          <p className="mb-5 w-[10rem] text-center">우리는{" "}</p>
          <span className="text-orange-500 font-extrabold text-[8rem] align-bottom">포용</span>{" "}
          <p className="mb-5 w-[10rem] text-center">해야합니다</p>
        </h1>
        <p className="text-gray-700 mt-4">
          대한민국은 여러 불평등 문제로 점점 갈라져가고 있습니다.
          <br />
          우리 모두가 서로가 다름을 인정하고 더욱 따뜻한 마음으로 서로를 보듬어줘야합니다.
        </p>
        <div className="mt-10 text-sm sm:text-base text-black">
          <p className="font-semibold">
            we must <span className="text-orange-500 font-bold">embrace</span>
          </p>
          <p className="mt-1 text-gray-800">
            South Korea is becoming increasingly divided due to various inequalities.
            <br />
            We all need to acknowledge each other’s differences and embrace each other with warmer hearts.
          </p>
        </div>
      </div>
    </div>
  )
}