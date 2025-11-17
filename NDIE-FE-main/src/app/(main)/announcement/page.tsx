import { List } from "@/components/ui/list";

export default function Announcement(){
  return (
    <div className="flex items-center justify-center">
      <List name='공지사항' data='announcement'  />
    </div>
  )
}