import React from "react";

type ContainerProps = {
  children?: React.ReactNode;
  className?: string;
};

export default function ContentContainer({children, className}:ContainerProps) {
  return (
    <div className={`${className} h-[50rem] w-full overflow-hidden relative bg-[#F8F8F8] pl-40 pr-40`}>
      {children}
    </div>
  )
}