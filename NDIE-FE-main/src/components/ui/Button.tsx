import React from "react";
type ButtonVariant = 'primary' | 'outline';
type ButtonProps = {
  children?: React.ReactNode;
  variant?: ButtonVariant;
}
export default function Button ({children, variant="primary"}:ButtonProps) {
  const baseStyle = 'text-2xl px-[1.625rem] py-[0.8125rem] rounded-[0.75rem]';
  const variants = {
    primary: "bg-[#ED9735] text-[#FFFFFF]",
    outline: "bg-[#FFFFFF] text-[#000000] border-2 border-[#CFCFCF]",
  }
  return (
    <button className={`${baseStyle} ${variants[variant]}`}>
      {children}
    </button>
  )
}