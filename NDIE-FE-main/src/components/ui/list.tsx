'use client'

import axios from 'axios';
import React, { useState ,useEffect } from 'react';
import Listbox from '../layout/Listbox';
import { API_BASE } from '@/lib/config';
import Loading from './loading';

type ListProps = {
    name: string;
    data: string;
  };




  export function List({ name , data}: ListProps) {

    const [item,setitem] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
      setIsLoading(true);
      setError(null);

      axios.get(`${API_BASE}/${data}`)
        .then((response) => {
          setitem(response.data);
          setIsLoading(false);
        })
        .catch((error) => {
          console.error('데이터를 불러오는데 실패했습니다:', error);
          setError('데이터를 불러오는데 실패했습니다.');
          setIsLoading(false);
        });

    }, [data]);


    if (isLoading) {
      return (
        <div className="w-[80%] flex flex-col gap-[3vh] mt-[3vh] h-[90vh]">
          <p className="text-[3vh] ">{name}</p>
          <hr  className="border-[#CCCCCC] border-[1] rounded-[5]"/>
          <div className="flex justify-center items-center h-[70vh]">
            <Loading />
          </div>
        </div>
      );
    }

    if (error) {
      return (
        <div className="w-[80%] flex flex-col gap-[3vh] mt-[3vh] h-[90vh]">
          <p className="text-[3vh] ">{name}</p>
          <hr  className="border-[#CCCCCC] border-[1] rounded-[5]"/>
          <div className="flex justify-center items-center h-[70vh]">
            <p className="text-[2vh] text-red-500">{error}</p>
          </div>
        </div>
      );
    }

    if (!item || item.length === 0) {
      return (
        <div className="w-[80%] flex flex-col gap-[3vh] mt-[3vh] h-[90vh]">
          <p className="text-[3vh] ">{name}</p>
          <hr  className="border-[#CCCCCC] border-[1] rounded-[5]"/>
          <div className="flex justify-center items-center h-[70vh]">
            <p className="text-[2vh] text-gray-500">데이터가 없습니다.</p>
          </div>
        </div>
      );
    }

    return (
    <div className="w-[80%] flex flex-col gap-[3vh] mt-[3vh] h-[90vh]">
    <p className="text-[3vh] ">{name}</p>
    <hr  className="border-[#CCCCCC] border-[1] rounded-[5]"/>
    <div>
    <Listbox item={item} datas={data} name={name}/>
    </div>
    </div>

    );
  }
  
