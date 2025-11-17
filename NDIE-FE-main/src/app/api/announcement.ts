import axiosInstance from "@/lib/axiosInstance";

export const CreateAnnouncement = async (data: { title: string, content: string}) => {
  try {
    const res = await axiosInstance.post('/announcement', data);
    return res.status;
  } catch (error) {
    console.error('공지사항 작성 실패:', error);
    throw error;
  }
};