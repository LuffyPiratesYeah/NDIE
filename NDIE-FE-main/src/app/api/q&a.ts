import axiosInstance from "@/lib/axiosInstance";

export const CreateQA = async (data: { "title": string, "content": string }) => {
  try {
    const res = await axiosInstance.post('/QNA', data);
    return res.status;
  } catch (error) {
    console.error('Q&A 작성 실패:', error);
    throw error;
  }
};