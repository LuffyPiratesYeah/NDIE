import React from "react";

type TagPattern = {
  pattern: RegExp;
  component: (content: React.ReactNode, match?: RegExpMatchArray) => React.ReactNode;
  useRawInner?: boolean;
};

export default function makeDocument(text : string) {
  const tagPatterns: TagPattern[] = [
    {
      pattern: /^####(.+)$/m,
      component: (content) => <span className="text-[17px] font-semibold">{content}</span>
    },
    {
      pattern: /^###(.+)$/m,
      component: (content) => <span className="text-[20px] font-semibold">{content}</span>
    },
    {
      pattern: /^##(.+)$/m,
      component: (content) => <span className="text-[25px] font-semibold">{content}</span>
    },
    {
      pattern: /^#(.+)$/m,
      component: (content) => <span className="text-[27px] font-bold">{content}</span>
    },
    {
      pattern: /\*\*(.*?)\*\*/,
      component: (content) => <strong>{content}</strong>
    },
    {
      pattern: /__(.*?)__/,
      component: (content) => <span className="underline">{content}</span>
    },
    {
      pattern:  /\*(.*?)\*/,
      component: (content) => <i>{content}</i>,
    },
    {
      pattern:  /~~(.*?)~~/,
      component: (content) => <span className="line-through">{content}</span>,
    },
    {
      pattern:/---(.*?)/,
      component: () => <div className="w-full h-[2px] bg-gray-500 my-1"></div>
    },
    {
      pattern:/<이미지 src="(.*?)"><\/이미지>/,
      component : (_content, match) => {
        const src = match?.[1];
        if(!src) return null;
        return <img className="object-cover block mx-auto max-h-[300px] w-full" src={src} alt="추가된이미지"/>;
      },
      useRawInner: true
    },
  ];

  function parseText(inputText : string) : React.ReactNode {
    if (!inputText) return null;
    let earliestMatch: RegExpMatchArray | null = null;
    let matchComponent: TagPattern["component"] | null = null;
    let useRawInner = false;

    tagPatterns.forEach(({ pattern, component, useRawInner: rawFlag }) => {
      const match = inputText.match(pattern);
      if (match && (!earliestMatch || (match.index ?? 0) < (earliestMatch.index ?? Infinity))) {
        earliestMatch = match;
        matchComponent = component;
        useRawInner = !!rawFlag;
      }
    });

    if (!earliestMatch){
      return inputText.split("\n").map((line, index) => (
        <span key={index} className="w-full break-words flex min-h-max items-center flex-wrap my-[5px]">{line}</span>
      ));
    }

    const resolvedMatch = earliestMatch as RegExpMatchArray;
    const matchIndex = resolvedMatch.index ?? 0;
    const fullMatch = resolvedMatch[0];
    const innerText = resolvedMatch[1] ?? "";
    const prefixText = inputText.slice(0, matchIndex);
    const suffixText = inputText.slice(matchIndex + fullMatch.length);

    if(!matchComponent) return <span className="w-full break-words flex min-h-max items-center flex-wrap my-[5px]">{innerText}</span>
    const componentFn: TagPattern["component"] = matchComponent;

    const contentForComponent = useRawInner ? innerText : parseText(innerText);

    return (
      <>
        {prefixText && parseText(prefixText)}
        {componentFn(contentForComponent, resolvedMatch)}
        {suffixText && parseText(suffixText)}
      </>
    );
  }

  return <div className="flex-1 w-full flex flex-col flex-wrap">{parseText(text)}</div>;
}
