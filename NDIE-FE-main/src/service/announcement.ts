import axios from "@/lib/axiosInstance";

export async function getAnnouncement() {
  const response = await axios.get("/announcement");
  return response.data;
}