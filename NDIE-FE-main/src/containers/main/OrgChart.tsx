import React from "react";

type OrgNode = {
  name: string;
  level: number;
  child?: OrgNode[];
};
const orgTreeData: OrgNode = {
  name: "root",
  level: 0,
  child: [{
      name: "child",
      level: 1,
      child: [
        {
          name: "child2",
          level: 2
        },{
          name: "child2",
          level: 2
        },{
          name: "child2",
          level: 2,
          child: [
            {
              name: "child3.1",
              level: 3
            },
            {
              name: "child3.2",
              level: 3
            },
            {
              name: "child3.3",
              level: 3
            }
          ]
        }
      ]
    }
  ]
}


export default function OrgChart() {
  return (
    <div className="flex flex-col items-center py-12">
      <OrgNodeComponent node={orgTreeData} />
    </div>
  );
}
type orgNodeComponentProps = {
  node: OrgNode;
}
const OrgNodeComponent = ({node}:orgNodeComponentProps) => {

  return (
    <div className="flex flex-col items-center gap-4 text-2xl">
      <div className={`w-[12.5rem] h-[5rem] border-2 ${node.level == 0 ? "bg-[#ED9735] border-[#BD894D]":node.level==1?"bg-[#F4AA55] border-[#ED9735]":node.level == 2?"bg-[#FCC07C] border-[#F4AA55]":"bg-[#FFFFFF] border-[#FCC07C]"} flex justify-center items-center rounded-md`}>
        {node.name}
      </div>
      <div className={`flex ${node.level == 2 ? 'flex-col' :  'flex-row gap-6'}`}>
      {node.child?.map((child: OrgNode,index) => {
        return <OrgNodeComponent key={index} node={child} />
      })}
      </div>
    </div>
  )
}