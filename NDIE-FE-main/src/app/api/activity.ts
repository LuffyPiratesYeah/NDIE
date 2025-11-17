import axiosInstance from "@/lib/axiosInstance";

export const CreateActivity = async (data: { title: string, content: string , image: string}) => {
  try {
    const res = await axiosInstance.post('/activity', data);
    return res.status;
  } catch (error) {
    console.error('활동 작성 실패:', error);
    throw error;
  }
};

export const uploadImg = async (data: FormData) => {
  try {
    const res = await axiosInstance.post('/upload', data, {
      headers:{
        'Content-Type': 'multipart/form-data'
      }
    });
    return res.data;
  } catch (error) {
    console.error('이미지 업로드 실패:', error);
    throw error;
  }
};