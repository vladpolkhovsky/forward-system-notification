import { createFetch } from "@vueuse/core";
import { useAuth } from "./useAuth";

const { token } = useAuth();

const useFetchWithAuth = createFetch({
  combination: "overwrite",
  options: {
    async beforeFetch({ options }) {
      options.headers.Authorization = `Bearer ${token}`;
      return { options };
    },
  },
});

export default useFetchWithAuth;
