import Image from "next/image";
import Logo from "@public/images/logo.svg"
import React from "react";

type FooterButtonProps = {
  children: React.ReactNode;
};
const FooterButton = ({children}:FooterButtonProps) => {
  return (
    <button className="bg-white text-black p-[1.125rem]">
      {children}
    </button>
  )
}
export const Footer = () => {
  return (
    <footer className="h-[36.25rem] w-full bg-[#001246] pl-40 pr-40 flex justify-between text-white items-center text-sm">
      <ul className="flex gap-4 flex-col">
        <li>부산광역시 동래구 온천천로471번가길 40</li>
        <li>대표이사 이성철</li>
        <li>사무총장 박영민</li>
        <div className="flex gap-11 items-center">
          <FooterButton>국세청</FooterButton>
          <FooterButton>국민권익위원회</FooterButton>
          <FooterButton>서울시교육청</FooterButton>
        </div>
        <ul className="flex gap-4 items-center">
          <li>연구</li>
          <li>교육</li>
          <li>소통</li>
          <li>회비/후원</li>
        </ul>
        <ul className="flex gap-11 items-center">
          <li>이용약관</li>
          <li>개인정보처리방침</li>
        </ul>
        <li>NDIE 디지털과 포용성 네트워크</li>
        <li>NDIE Network for Digital Inclusion and Empowerment</li>
      </ul>
      <Image src={Logo} alt={"Logo"} className="h-full w-1/5" />
    </footer>
  )
}