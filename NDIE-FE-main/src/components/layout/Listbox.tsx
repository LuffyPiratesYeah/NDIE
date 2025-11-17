"use client";

import axios from 'axios';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import Loading from '@/components/ui/loading'; 
import { API_BASE } from '@/lib/config';

type ListboxProps = {
  item: { id: number; title: string; username: string; views: number; createdAt: string }[];
  datas: string;
  name: string;
};

export default function Listbox({ item, datas, name }: ListboxProps) {
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredItems, setFilteredItems] = useState(item);
  const [hasSearched, setHasSearched] = useState(false); // 🔸 추가

  const handleSearch = () => {
    setHasSearched(true); 
    const filtered = item.filter((i) =>
      i.title.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredItems(filtered);
  };

  function formatDate(dateStr: string) {
    const [year, month, day] = dateStr.split('T')[0].split('-');
    return `${parseInt(year)}년 ${parseInt(month)}월 ${parseInt(day)}일`;
  }



  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredItems(item); 
      setHasSearched(false); 
    }
  }, [searchTerm, item]);

  const deslist = (id: number) => {
    sessionStorage.setItem('name', name);
    axios.get(`${API_BASE}/document/up/${id}`);
    axios
      .get(`${API_BASE}/${datas}/${id}`)
      .then(() => {
        router.push(`/${datas}/${id}`);
      })
  };

  return (
    <div className="w-full px-4">
      <div className="mb-7 flex flex-row items-center justify-between">
        <p className="text-sm font-semibold">전체 {filteredItems.length} 건</p>

        <div className="flex flex-row gap-2">
          <input
            type="text"
            className="bg-[#F7F7F7] border border-[#DCDCDC] rounded-[1vh] w-[16vh] h-[3.5vh] text-center outline-none"
            placeholder="제목 검색"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button
            className="bg-[#ED9735] w-[8vh] h-[3.5vh] rounded-[1vh] text-white"
            onClick={handleSearch}
          >
            검색
          </button>
        </div>
      </div>

      <hr className="border border-black my-1" />
      <div className="grid grid-cols-5 text-center font-semibold py-2 border-b border-gray-400">
        <p>번호</p>
        <p>제목</p>
        <p>작성자</p>
        <p>등록일</p>
        <p>조회</p>
      </div>

      {filteredItems.length > 0 ? (
        filteredItems.map((i) => (
          <div
            key={i.id}
            className="grid grid-cols-5 text-center py-2 border-b border-gray-200 text-sm cursor-pointer"
            onClick={() => deslist(i.id)}
          >
            <p>{i.id}</p>
            <p className="truncate">{i.title}</p>
            <p>{i.username}</p>
            <p>{formatDate(i.createdAt)}</p>
            <p>{i.views}</p>
          </div>
        ))
      ) : hasSearched ? (
        <div className="text-center py-4 text-gray-500">검색 결과가 없습니다.</div>
      ) : <Loading />}
    </div>
  );
}
